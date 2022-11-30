package com.example.atamerica.controllers;

import android.util.Log;

import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.taskhandler.QueryVwAllEventTask;
import com.example.atamerica.taskhandler.TaskRunner;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class BookedController {

    public static class GetEvents implements Callable<List<VwAllEventModel>> {

        private final boolean isRefresh;

        public GetEvents(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        public List<VwAllEventModel> call() {
            if (isRefresh || HelperClass.isEmpty(EventItemCache.RegisteredEventList)) {
                List<String> registeredEventIds = DataHelper.Query.ReturnAsList("SELECT EventId FROM MemberRegister WHERE Email = ? ORDER BY RegisterDate; ", new Object[] {AccountManager.User.Email});

                EventItemCache.RegisteredEventList.clear();

                if (!HelperClass.isEmpty(registeredEventIds)) {
                    for (String eventId : registeredEventIds) {
                        // Check cache
                        if (EventItemCache.EventCacheMap.containsKey(eventId)) {
                            EventItemCache.RegisteredEventList.add(EventItemCache.EventCacheMap.get(eventId));
                        }
                        else {
                            try {
                                VwAllEventModel event = DataHelper.Query.ReturnAsObject("SELECT * FROM VwAllEvent WHERE EventId = ?; ", VwAllEventModel.class, new Object[] { eventId });
                                event.MapAttribute();
                                event.MapDocument();

                                // Check for registration status
                                new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> event.Registered = (data2 != null && data2));

                                // Store information to cache
                                EventItemCache.EventCacheMap.put(event.EventId, event);
                                EventItemCache.RegisteredEventList.add(event);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            return EventItemCache.RegisteredEventList;
        }
    }

}