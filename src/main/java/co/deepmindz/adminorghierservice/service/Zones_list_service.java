package co.deepmindz.adminorghierservice.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.CreateZoneListDTO;
import co.deepmindz.adminorghierservice.dto.Zones_list_RequestDto;
import co.deepmindz.adminorghierservice.dto.Zones_list_ResponseDto;
import co.deepmindz.adminorghierservice.dto.Zones_list_with_parentZone_response;
import co.deepmindz.adminorghierservice.models.Zones_list;

@Service
public interface Zones_list_service {

	List<Zones_list_ResponseDto> getAllZonesList(String[] zoneIds);

	CreateZoneListDTO createZones(CreateZoneListDTO zoneDto);

	Zones_list_ResponseDto listZoneById(String zoneId);

	Zones_list_ResponseDto deleteZones(String zoneId);

	Zones_list_ResponseDto updateZone(Zones_list_RequestDto zones_listDto);

	public void cleanAllZone_list();

	List<Zones_list_with_parentZone_response> getAllZonesByRelationshipId(String linked_zone);

	JSONObject getAllZoneListByRelationshipId(String linked_zone, String getParent_zone_list_id);

	List<Zones_list> getCordinatorByLinkedZoneId(List<String> zoneId);

	String getFeedbackToByPhc(String zoneId);

	void updateTotalVisitofAllZones(List<String> ids);
}
