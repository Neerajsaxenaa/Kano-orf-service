package co.deepmindz.adminorghierservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.ParentZoneDTO;
import co.deepmindz.adminorghierservice.dto.ZoneListFiltrationResponseDTO;
import co.deepmindz.adminorghierservice.dto.ZonesResponseDto;
import co.deepmindz.adminorghierservice.models.Zones_list;
import co.deepmindz.adminorghierservice.repository.ZoneRepo;
import co.deepmindz.adminorghierservice.repository.Zones_list_Repo;
import co.deepmindz.adminorghierservice.service.ZoneListService;
import co.deepmindz.adminorghierservice.utils.AvailableZone;
import co.deepmindz.adminorghierservice.utils.ZonesUtil;

@Service
public class ZoneListImpl implements ZoneListService {

	@Autowired
	Zones_list_Repo zonesListRepo;

	@Autowired
	private ZonesUtil zonesUtil;

	@Autowired
	private ZoneRepo zoneRepo;

	@Override
	public List<ZoneListFiltrationResponseDTO> getFiltrationZoneList(String belongs_to_id) {
		List<ZonesResponseDto> allAvailableZones = getZoneAndParents(belongs_to_id);
		ZonesResponseDto belongsToZone = allAvailableZones.get(allAvailableZones.size() - 1);

		List<Zones_list> availableCurrentZoneList = zonesListRepo.getZonesUsingLinkedZonesUsingInClause(
				List.of(allAvailableZones.get(allAvailableZones.size() - 1).getZone_id()));

		List<AvailableZone> availableZones = new ArrayList<>();
		for (Zones_list zone : availableCurrentZoneList) {
			availableZones.add(new AvailableZone(zone.getName(), zone.get_id(), zone.getCode()));
		}

		ZoneListFiltrationResponseDTO zoneListFiltrationResponseDTO = new ZoneListFiltrationResponseDTO(
				allAvailableZones.size(), belongsToZone.getName(), belongsToZone.getZone_id(), availableZones);
		List<ZoneListFiltrationResponseDTO> finalItem = new ArrayList<>();
		finalItem.add(zoneListFiltrationResponseDTO);

		return finalItem;
	}

	public List<ZonesResponseDto> getZoneAndParents(String zoneId) {
		List<ZonesResponseDto> zones = new ArrayList<>();
		ZonesResponseDto zone = getZoneById(zoneId);
		if (zone != null) {
			zones.add(zone);
			if (zone.getParentZone_id() != null && !zone.getParentZone_id().isEmpty()) {
				zones.addAll(getZoneAndParents(zone.getParentZone_id()));
			}
		}
		return zones;
	}

	public ZonesResponseDto getZoneById(String zoneId) {
		return zonesUtil.mapEntityToResponseDto(zoneRepo.findById(zoneId).orElse(null));
	}

	@Override
	public List<AvailableZone> getAllListZonesByLinkedZoneList(String zoneListFiltrationDTO) {
		List<AvailableZone> availableZones = new ArrayList<>();
		List<Zones_list> allZonesByRelationshipId = zonesListRepo
				.getAllZonesByRelationshipId(zoneListFiltrationDTO.toString());
		if (!allZonesByRelationshipId.isEmpty() && allZonesByRelationshipId != null) {
			for (Zones_list zone : allZonesByRelationshipId) {
				availableZones.add(new AvailableZone(zone.getName(), zone.get_id(), zone.getCode()));
			}
			return availableZones;
		}
		return availableZones;

	}

	@Override
	public List<ParentZoneDTO> getParentZoneList() {
		List<ParentZoneDTO> item = zonesListRepo.getParentZoneItem();
		return item;
	}
}
