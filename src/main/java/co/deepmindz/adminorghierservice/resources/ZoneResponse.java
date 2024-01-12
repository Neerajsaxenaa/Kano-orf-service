package co.deepmindz.adminorghierservice.resources;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class ZoneResponse {
        private List<Zone> zone;
        private List<Zone> linked_zone_list;

        // Constructors, getters, and setters
    }
