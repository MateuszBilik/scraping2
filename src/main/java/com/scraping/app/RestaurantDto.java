package com.scraping.app;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    private String name;
    private String description;
    private int timeToDelivery;
    private String workingHours;
    private String address;

    @Override
    public String toString() {
        return  "\nname='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timeToDelivery='" + timeToDelivery + '\'' +
                ", workingHours='" + workingHours + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
