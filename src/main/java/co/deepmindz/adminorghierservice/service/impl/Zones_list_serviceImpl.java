package co.deepmindz.adminorghierservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.CreateZoneListDTO;
import co.deepmindz.adminorghierservice.dto.Zones_list_RequestDto;
import co.deepmindz.adminorghierservice.dto.Zones_list_ResponseDto;
import co.deepmindz.adminorghierservice.dto.Zones_list_with_parentZone_response;
import co.deepmindz.adminorghierservice.exception.ResourceNotFoundException;
import co.deepmindz.adminorghierservice.models.Zones;
import co.deepmindz.adminorghierservice.models.Zones_list;
import co.deepmindz.adminorghierservice.repository.ZoneRepo;
import co.deepmindz.adminorghierservice.repository.Zones_list_Repo;
import co.deepmindz.adminorghierservice.service.Zones_list_service;
import co.deepmindz.adminorghierservice.utils.Zones_list_util;

@Service
public class Zones_list_serviceImpl implements Zones_list_service {

	@Autowired
	private ZoneRepo zoneRepo;

	@Autowired
	private Zones_list_Repo zones_list_Repo;

	@Autowired
	private Zones_list_util zones_list_util;

	@Override
	public List<Zones_list_ResponseDto> getAllZonesList(String[] zoneIds) {
		List<Zones_list> allZonesList = null;
		if (zoneIds == null || zoneIds.length <= 0)
			allZonesList = zones_list_Repo.findAll();
		else
			allZonesList = zones_list_Repo.findAllById(List.of(zoneIds));
		if (allZonesList == null)
			return null;
		return zones_list_util.mapEntityToResponseDto(allZonesList);
	}

	@Override
	public CreateZoneListDTO createZones(CreateZoneListDTO zoneDto) {
		Zones elementFound = zoneRepo.findById(zoneDto.getBelongs_to()).get();
		if (!elementFound.getZone_id().isEmpty()) {
			Zones_list zoneList = new Zones_list();
			zoneList.setLinked_zone_list(zoneDto.getZone_listing_id());
			zoneList.setBelongs_to_zone(zoneDto.getBelongs_to());
			zoneList.setCode(zoneDto.getCode());
			zoneList.setName(zoneDto.getName().toUpperCase());
			Zones_list createdZones = zones_list_Repo.save(zoneList);
			return zoneDto;
		} else {
			throw new ResourceNotFoundException(
					"Operation can not be satisfied due to unavailbility of Zone Relationship!", "", "");
		}
	}

	@Override
	public Zones_list_ResponseDto listZoneById(String zoneId) {
		Optional<Zones_list> findById = zones_list_Repo.findById(zoneId);
		if (findById.isPresent()) {
			return zones_list_util.mapEntityToResponseDto(findById);
		}
		return null;

	}

	@Override
	public Zones_list_ResponseDto deleteZones(String zoneId) {
		Zones_list_ResponseDto zones_list_ResponseDto = null;
		zones_list_Repo.deleteById(zoneId);
		return zones_list_ResponseDto;
	}

	@Override
	public Zones_list_ResponseDto updateZone(Zones_list_RequestDto zones_listDto) {
		Optional<Zones_list> zones_list = zones_list_Repo.findById(zones_listDto.getId());
		List<Zones_list> findAll = zones_list_Repo.findAll();
		if (zones_list.isEmpty() || zones_list == null) {
			return null;
		}

		Zones_list z_list = new Zones_list();

//		if (zones_listDto.getName().contentEquals(findAll.size()))) {
//			 z_list.setName("zone name already found..!!");
////			 z_list.se
//			 return zones_list_util.mapEntityToResponseDto(z_list);
////			return "";
//		}

		z_list.set_id(zones_list.get().get_id());
		z_list.setName(zones_listDto.getName().toUpperCase());
		z_list.setBelongs_to_zone(zones_list.get().getBelongs_to_zone());
		z_list.setLinked_zone_list(zones_list.get().getLinked_zone_list());
		z_list.setCode(zones_list.get().getCode());
		zones_list_Repo.save(z_list);
		return zones_list_util.mapEntityToResponseDto(z_list);
	}

	@Override
	public void cleanAllZone_list() {
		zones_list_Repo.deleteAll();

	}

	@Override
	public List<Zones_list_with_parentZone_response> getAllZonesByRelationshipId(String linked_zone) {
		List<Zones_list_with_parentZone_response> mapListEntityToListResponseDto = null;
		try {
			List<Zones_list> allZonesByRelationshipId = zones_list_Repo.getAllZonesByRelationshipId(linked_zone);
			Optional<Zones> zones = zoneRepo.findById(linked_zone);
			String parentZonename = zones.get().getName();
			System.out.println("name :" + parentZonename);
			if (!allZonesByRelationshipId.isEmpty() && allZonesByRelationshipId != null) {

				mapListEntityToListResponseDto = zones_list_util
						.mapListEntityToParentListResponseDto(allZonesByRelationshipId, parentZonename);

			} else {
				throw new ResourceNotFoundException(linked_zone, linked_zone, "not found");
			}
		} catch (Exception e) {
			throw new ResourceNotFoundException("linked_zone", linked_zone, "not found");
		}
		return mapListEntityToListResponseDto;

	}

	@Override
	public JSONObject getAllZoneListByRelationshipId(String linked_zone, String getParent_zone_list_id) {
		Zones elementFound = zoneRepo.findById(linked_zone).get();
		List<Zones_list> availableParentZoneLists = null;
		if (!elementFound.getZone_id().isEmpty()) {
			if (getParent_zone_list_id == null || getParent_zone_list_id.isEmpty()) {
				availableParentZoneLists = new ArrayList<>();
			} else {
				availableParentZoneLists = zones_list_Repo.getListParentZoneItem(getParent_zone_list_id);
			}
			List<Zones_list> availableCurrentZoneList = zones_list_Repo
					.getZonesUsingLinkedZonesUsingInClause(List.of(elementFound.getZone_id()));
			if (!availableParentZoneLists.isEmpty()) {
				String firstElementId = availableParentZoneLists.get(0).get_id();
				availableCurrentZoneList.removeIf(zone -> !firstElementId.equals(zone.getLinked_zone_list()));
			}

			JSONObject response = constructResponse(availableParentZoneLists, availableCurrentZoneList);
			return response;
		} else {
			throw new ResourceNotFoundException("linked_zone", linked_zone, "not found");
		}
	}

	public JSONObject constructResponse(List<Zones_list> availableParentZoneLists,
			List<Zones_list> availableCurrentZoneList) {
		JSONArray zoneArray = new JSONArray();
		JSONArray linkedZoneArray = getJsonArray(availableParentZoneLists);

		for (Zones_list zone : availableCurrentZoneList) {
			JSONObject zoneObject = new JSONObject();
			zoneObject.put("_id", zone.get_id());
			zoneObject.put("name", zone.getName());
			zoneObject.put("linked_zone_list", zone.getLinked_zone_list());
			zoneObject.put("code", zone.getCode());
			zoneObject.put("belongs_to_zone", zone.getBelongs_to_zone());
			zoneArray.put(zoneObject);
		}

		JSONObject response = new JSONObject();
		response.put("zone", zoneArray);
		response.put("linked_zone_list", linkedZoneArray);
		return response;
	}

	private static JSONArray getJsonArray(List<Zones_list> availableParentZoneLists) {
		JSONArray linkedZoneArray = new JSONArray();
		for (Zones_list zone : availableParentZoneLists) {
			JSONObject zoneObject = new JSONObject();
			zoneObject.put("_id", zone.get_id());
			zoneObject.put("name", zone.getName());
			zoneObject.put("linked_zone_list", zone.getLinked_zone_list());
			zoneObject.put("code", zone.getCode());
			zoneObject.put("belongs_to_zone", zone.getBelongs_to_zone());
			linkedZoneArray.put(zoneObject);
		}
		return linkedZoneArray;
	}

	@Override
	public List<Zones_list> getCordinatorByLinkedZoneId(List<String> zoneId) {
		List<Zones_list> findAllById = zones_list_Repo.findAllById(zoneId);
		return findAllById;
	}

	@Override
	public List<String> getFeedbackToByPhc(String zoneId) {
		List<String> feedbackTo = new ArrayList<>();
		String feedbackToByPhc = zones_list_Repo.getFeedbackToByPhc(zones_list_Repo.getParentZone(zoneId));
		feedbackTo.add(feedbackToByPhc);
		return feedbackTo;
	}

	@Override
	public void updateTotalVisitofAllZones(List<String> ids) {
		zones_list_Repo.updateTotalVisitforAllZones(ids);
	}
}
