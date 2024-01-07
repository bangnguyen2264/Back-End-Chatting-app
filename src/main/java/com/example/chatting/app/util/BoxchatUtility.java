package com.example.chatting.app.util;

import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
public class BoxchatUtility {
    public static User getUserFromPrincipal(Principal connectedUser) {
        return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }

    public static List<Boxchat> filterBoxchatsByMember(User user, List<Boxchat> boxchats) {
        return boxchats.stream()
                .filter(boxchat -> boxchat.getMembers().contains(user))
                .collect(Collectors.toList());
    }

}
