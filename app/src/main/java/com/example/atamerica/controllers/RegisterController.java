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
            if (EventItemCache.UserRegisteredEventList.contains(eventId)) {
                return true;
            }
            else {
                int registered = DataHelper.Query.ReturnAsInt("SELECT COUNT(*) AS Cnt FROM MemberRegister WHERE EventId = ? AND Email = ?; ", new Object[] { eventId, email });
                if (registered > 0) {
                    EventItemCache.UserRegisteredEventList.add(eventId);
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

        /**
         * @return String,
         * "A" indicates success
         * "B" indicates error
         * "C" indicates exists
         */
        @Override
        public String call() {
            try {
                // Check if already registered
                if (EventItemCache.UserRegisteredEventList.contains(eventId)) {
                    returnStatus = "C";
                }
                else {
                    boolean execute = DataHelper.Query.ExecuteNonQuery("INSERT INTO MemberRegister (EventId, Email, RegisterDate, RegistrationStatus) VALUES (?, ?, NOW(), 'A'); ", new Object[] { eventId, email });

                    if (execute) {
                        // Rsync event cache
                        if (EventItemCache.EventCacheMap.containsKey(eventId)) {
                            Objects.requireNonNull(EventItemCache.EventCacheMap.get(eventId)).Registered = true;

                            EventItemCache.RegisteredEventList.remove(EventItemCache.EventCacheMap.get(eventId));
                            EventItemCache.RegisteredEventList.add(EventItemCache.EventCacheMap.get(eventId));

                            EventItemCache.UserRegisteredEventList.remove(eventId);
                            EventItemCache.UserRegisteredEventList.add(eventId);
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

        /**
         * @return String,
         * "A" indicates success
         * "B" indicates error
         * "C" indicates doesn't exists
         */
        @Override
        public String call() {
            try {
                // Check if already registered
                if (EventItemCache.UserRegisteredEventList.contains(eventId)) {
                    boolean execute = DataHelper.Query.ExecuteNonQuery("DELETE FROM MemberRegister WHERE EventId = ? AND Email = ?;  ", new Object[] { eventId, email });
                    if (execute) {
                        // Rsync event cache
                        if (EventItemCache.EventCacheMap.containsKey(eventId)) {
                            Objects.requireNonNull(EventItemCache.EventCacheMap.get(eventId)).Registered = false;
                            EventItemCache.RegisteredEventList.remove(EventItemCache.EventCacheMap.get(eventId));
                            EventItemCache.UserRegisteredEventList.remove(eventId);
                        }
                    }
                    else {
                        returnStatus = "B";
                    }
                }
                else {
                    returnStatus = "C";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return returnStatus;
        }
    }
}
