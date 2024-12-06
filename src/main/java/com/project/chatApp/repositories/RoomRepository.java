package com.project.chatApp.repositories;

import com.project.chatApp.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room,String> {
    //get room using roomId
    Room findByRoomId(String roomId);
}
