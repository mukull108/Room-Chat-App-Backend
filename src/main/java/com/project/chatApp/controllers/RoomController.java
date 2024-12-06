package com.project.chatApp.controllers;

import com.project.chatApp.entities.Message;
import com.project.chatApp.entities.Room;
import com.project.chatApp.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("room-chat-app-frontend-production.up.railway.app")
public class RoomController {
    @Autowired
    private RoomRepository repository;

    Logger logger= LoggerFactory.getLogger(RoomController.class);
    //create room
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId){
        if(repository.findByRoomId(roomId) != null){
            //room is already available
            return ResponseEntity.badRequest().body("Room is already available with the given data!");
        }
        //create room
        Room room = new Room();
        room.setRoomId(roomId);
        Room savedroom = repository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    //get room: trying to join the room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId){
        logger.info("Room Id you entered is: {}",roomId);
        StringBuilder str = new StringBuilder(roomId);
        str.append('=');
        Room room = repository.findByRoomId(str.toString());
        if(room == null){
            return ResponseEntity.badRequest().body("Room is not available with the given Id!");
        }
        return ResponseEntity.ok(room);
    }

    //get messages of room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page",defaultValue = "0",required = false) int page,
            @RequestParam(value = "size",defaultValue = "20",required = false) int size
    ){
        Room room  = repository.findByRoomId(roomId);
        if(room == null){
            return ResponseEntity.badRequest().build();
        }

        //get messages, pagination
        List<Message> messageList = room.getMessageList();
        int start = Math.max(0,messageList.size()-(page+1)*size);
        int end = Math.min(messageList.size(),start+size);

        List<Message> paginatedList = messageList.subList(start, end);
        return ResponseEntity.ok(paginatedList);

    }
}
