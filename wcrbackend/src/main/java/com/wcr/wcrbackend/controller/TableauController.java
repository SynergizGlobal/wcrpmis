package com.wcr.wcrbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/tableau")
public class TableauController {

    @Value("${tableau.server.url:http://10.48.192.7:8000}")
    private String tableauServerUrl; 

    @Value("${tableau.trusted.username:SynTrack}")
    private String tableauUsername; 

    @PostMapping("/ticket")
    public ResponseEntity<String> getTrustedTicket(
            @RequestParam(name = "site", required = false) String site,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "client_ip", required = false) String clientIp) {
        try {
            String user = username != null ? username : tableauUsername;
            String targetSite = site != null ? site : "";
            String ip = clientIp != null ? clientIp : "";

            URL url = new URL(tableauServerUrl + "/trusted/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "username=" + user;
            if (!targetSite.isEmpty()) postData += "&target_site=" + targetSite;
            if (!ip.isEmpty()) postData += "&client_ip=" + ip;

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ticket = in.readLine();
            in.close();

            System.out.println("Trusted Ticket: " + ticket); // Log for debugging

            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error getting trusted ticket");
        }
    }
}
