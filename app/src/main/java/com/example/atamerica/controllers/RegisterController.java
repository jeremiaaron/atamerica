package com.example.atamerica.controllers;

import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;

import java.util.Objects;
import java.util.concurrent.Callable;

public class RegisterController {

    public static class CheckRegister implements Callable<Boolean> {

        private final String email;
        private final String eventId;

        public CheckRegister(String email, String eventId) {
            this.email = email;
            this.eventId = eventId;
        }

        @Override
        public Boolean call() {
            if (EventItemCache.UserRegisteredEventList.contains(email + eventId)) {
                return true;
            }
            else {
                int registered = DataHelper.Query.ReturnAsInt("SELECT COUNT(*) AS Cnt FROM MemberRegister WHERE EventId = ? AND Email = ?; ", new Object[] { eventId, email });
                if (registered > 0) {
                    EventItemCache.UserRegisteredEventList.add(email + eventId);
                    return true;
                }
            }

            return false;
        }
    }

    public static class RegisterUser implements Callable<String> {

        private String returnStatus = "A";
        private final String email;
        private final String eventId;

        public RegisterUser(String email, String eventId) {
            this.email = email;
            this.eventId = eventId;
        }

        @Override
        public String call() {
            try {
                // Check if already registered
                if (EventItemCache.UserRegisteredEventList.contains(email + eventId)) {
                    returnStatus = "B";
                }
                else {
                    boolean execute = DataHelper.Query.ExecuteNonQuery("INSERT INTO MemberRegister (EventId, Email, RegisterDate, RegistrationStatus) VALUES (?, ?, NOW(), 'A'); ", new Object[] { eventId, email });

                    if (execute) {
                        // Rsync event cache
                        if (EventItemCache.EventCacheMap.containsKey(eventId)) {
                            Objects.requireNonNull(EventItemCache.EventCacheMap.get(eventId)).Registered = true;

                            EventItemCache.UserRegisteredEventList.remove(email + eventId);
                            EventItemCache.UserRegisteredEventList.add(email + eventId);
                        }
                    }
                    else {
                        returnStatus = "B";
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return returnStatus;
        }
    }

    public static class UnregisterUser implements Callable<String> {

        private String returnStatus = "A";
        private final String email;
        private final String eventId;

        public UnregisterUser(String email, String eventId) {
            this.email = email;
            this.eventId = eventId;
        }

        @Override
        public String call() {
            try {
                // Check if already registered
                if (EventItemCache.UserRegisteredEventList.contains(email + eventId)) {
                    boolean execute = DataHelper.Query.ExecuteNonQuery("DELETE FROM MemberRegister WHERE EventId = ? AND Email = ?;  ", new Object[] { eventId, email });
                    if (execute) {
                        // Rsync event cache
                        if (EventItemCache.EventCacheMap.containsKey(eventId)) {
                            Objects.requireNonNull(EventItemCache.EventCacheMap.get(eventId)).Registered = false;
                            EventItemCache.UserRegisteredEventList.remove(email + eventId);
                        }
                    }
                    else {
                        returnStatus = "B";
                    }
                }
                else {
                    returnStatus = "B";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return returnStatus;
        }
    }
}
