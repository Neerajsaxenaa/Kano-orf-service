package co.deepmindz.adminorghierservice.service;

import java.util.List;

import co.deepmindz.adminorghierservice.dto.ParentZoneDTO;
import co.deepmindz.adminorghierservice.dto.ZoneListFiltrationResponseDTO;
import co.deepmindz.adminorghierservice.utils.AvailableZone;

public interface ZoneListService {
    List<ZoneListFiltrationResponseDTO> getFiltrationZoneList(String zoneListFiltrationDTO);

    List<AvailableZone> getAllListZonesByLinkedZoneList(String zoneListFiltrationDTO);

    List<ParentZoneDTO> getParentZoneList();

}
