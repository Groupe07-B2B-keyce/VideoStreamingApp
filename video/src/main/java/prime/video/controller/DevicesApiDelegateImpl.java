package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.DevicesApiDelegate;
import prime.video.model.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DevicesApiDelegateImpl implements DevicesApiDelegate {

    @Override
    public ResponseEntity<List<Device>> listDevices() {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> revokeDevice(UUID deviceId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
