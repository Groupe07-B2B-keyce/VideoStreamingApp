package prime.video.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/p2p")
public class P2PSignalingController {

    private final Map<String, List<SignalMessage>> rooms = new ConcurrentHashMap<>();

    @GetMapping
    public String p2pPage(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Pair to pair");
        model.addAttribute("peerName", principal != null ? principal.getName() : "invite");
        return "p2p";
    }

    @PostMapping("/rooms")
    @ResponseBody
    public RoomResponse createRoom() {
        String roomId = UUID.randomUUID().toString().substring(0, 8);
        rooms.putIfAbsent(roomId, new ArrayList<>());
        return new RoomResponse(roomId);
    }

    @GetMapping("/rooms/{roomId}")
    @ResponseBody
    public ResponseEntity<RoomResponse> roomExists(@PathVariable String roomId) {
        if (!rooms.containsKey(roomId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RoomResponse(roomId));
    }

    @PostMapping("/rooms/{roomId}/signals")
    @ResponseBody
    public ResponseEntity<SignalMessage> publishSignal(@PathVariable String roomId,
                                                       @RequestBody SignalRequest request) {
        rooms.putIfAbsent(roomId, new ArrayList<>());

        SignalMessage message = new SignalMessage(
                request.from(),
                request.to(),
                request.type(),
                request.payload(),
                Instant.now().toString()
        );

        List<SignalMessage> signals = rooms.get(roomId);
        synchronized (signals) {
            signals.add(message);
            if (signals.size() > 300) {
                signals.subList(0, signals.size() - 300).clear();
            }
        }

        return ResponseEntity.ok(message);
    }

    @GetMapping("/rooms/{roomId}/signals")
    @ResponseBody
    public ResponseEntity<SignalBatch> pollSignals(@PathVariable String roomId,
                                                   @RequestParam String peerId,
                                                   @RequestParam(defaultValue = "0") int after) {
        List<SignalMessage> signals = rooms.get(roomId);
        if (signals == null) {
            return ResponseEntity.notFound().build();
        }

        List<SignalMessage> unread = new ArrayList<>();
        int nextIndex;
        synchronized (signals) {
            int start = Math.max(0, Math.min(after, signals.size()));
            for (int i = start; i < signals.size(); i++) {
                SignalMessage signal = signals.get(i);
                boolean isOwnMessage = peerId.equals(signal.from());
                boolean isForPeer = signal.to() == null || signal.to().isBlank() || peerId.equals(signal.to());
                if (!isOwnMessage && isForPeer) {
                    unread.add(signal);
                }
            }
            nextIndex = signals.size();
        }

        return ResponseEntity.ok(new SignalBatch(nextIndex, unread));
    }

    public record RoomResponse(String roomId) {}

    public record SignalRequest(String from, String to, String type, String payload) {}

    public record SignalMessage(String from, String to, String type, String payload, String createdAt) {}

    public record SignalBatch(int nextIndex, List<SignalMessage> messages) {}
}
