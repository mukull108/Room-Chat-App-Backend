package com.project.chatApp.controllers;

import com.project.chatApp.entities.Message;
import com.project.chatApp.entities.Room;
import com.project.chatApp.payload.MessageRequest;
import com.project.chatApp.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
@CrossOrigin("http://localhost:5173")
public class ChatController {
    @Autowired
    public RoomRepository roomRepository;

    //methods for sending and receiving messages
    @MessageMapping("/sendMessage/{roomId}")// /app/sendMessage/roomId
    @SendTo("/topic/room/{roomId}")//subscribe
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ){
        Room room = roomRepository.findByRoomId(request.getRoomId());

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setDateTime(LocalDateTime.now());

        if(room != null){
            room.getMessageList().add(message);
            roomRepository.save(room);
        }else{
            throw new RuntimeException("Room not found!");
        }
        return message;

    }


}
