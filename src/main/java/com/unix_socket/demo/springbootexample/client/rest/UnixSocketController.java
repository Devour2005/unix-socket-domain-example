package com.unix_socket.demo.springbootexample.client.rest;

import com.unix_socket.demo.springbootexample.client.service.MessageSenderService;
import com.unix_socket.demo.springbootexample.client.service.MessageSenderServiceNolib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnixSocketController {

    @Autowired
    private MessageSenderService senderService;
    @Autowired
    private MessageSenderServiceNolib nolibSenderService;

    @GetMapping("/lib-endpoint")
    public String libEndpoint() {
        senderService.connectAndSendMessage();
        return "libEndpoint";
    }

    @GetMapping("/nolib-endpoint")
    public String nolibEndpoint() {
        nolibSenderService.connectAndSendMessage();
        return "nolibEndpoint";
    }
}
