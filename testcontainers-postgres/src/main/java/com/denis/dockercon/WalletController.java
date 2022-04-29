package com.denis.dockercon;

import com.denis.dockercon.entity.Wallet;
import com.denis.dockercon.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @CrossOrigin(value="*")
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallet> save(@RequestBody final Wallet wallet) {

        try {
            Wallet result = walletService.save(wallet);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value="*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallet> getProfile(@PathVariable("id") Long id) {
        Wallet result = walletService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
