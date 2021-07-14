package com.terrafunder.terrafunder.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class RainEvent implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        if(event.toWeatherState()) event.setCancelled(true);
    }
}
