package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.VoiceAssistantApiDelegate;
import prime.video.model.VoiceCommandRequest;
import prime.video.model.VoiceCommandResponse;

@Service
public class VoiceAssistantApiDelegateImpl implements VoiceAssistantApiDelegate {

    @Override
    public ResponseEntity<VoiceCommandResponse> processVoiceCommand(VoiceCommandRequest voiceCommandRequest) {
        VoiceCommandResponse response = new VoiceCommandResponse();
        response.setAction(VoiceCommandResponse.ActionEnum.PLAY);
        response.setTargetId("e4fc1fbb-1b34-5656-f7fb-6fb33a2a3707");
        response.setMessage("D'accord, je lance votre contenu.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
