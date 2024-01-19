package co.deepmindz.adminorghierservice.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.deepmindz.adminorghierservice.dto.AllZonesByRelationshipIdDTO;
import co.deepmindz.adminorghierservice.dto.CreateZoneListDTO;
import co.deepmindz.adminorghierservice.dto.CustomHttpResponse;
import co.deepmindz.adminorghierservice.dto.ParentZoneDTO;
import co.deepmindz.adminorghierservice.dto.ZoneListFiltrationDTO;
import co.deepmindz.adminorghierservice.dto.ZoneListFiltrationResponseDTO;
import co.deepmindz.adminorghierservice.dto.Zones_list_RequestDto;
import co.deepmindz.adminorghierservice.dto.Zones_list_ResponseDto;
import co.deepmindz.adminorghierservice.models.Zones_list;
import co.deepmindz.adminorghierservice.service.SSUserService;
import co.deepmindz.adminorghierservice.service.ZoneListService;
import co.deepmindz.adminorghierservice.service.Zones_list_service;
import co.deepmindz.adminorghierservice.utils.AvailableZone;
import co.deepmindz.adminorghierservice.utils.CustomDataTypes.LinkedZoneIds;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/organization/zone-list")
public class Zones_list_Controller {

	@Autowired
	private Zones_list_service zones_list_service;

	@Autowired
	private ZoneListService zoneListService;
	
	@Autowired
	SSUserService ssUserService;

	@GetMapping("/get-all-zones")
	public ResponseEntity<Object> getAllZonesList() {
		List<Zones_list_ResponseDto> zones_list_ResponseDtos = zones_list_service.getAllZonesList(null);
		return CustomHttpResponse.responseBuilder("All available Zones", HttpStatus.OK, zones_list_ResponseDtos);

	}

//	@PostMapping("/get-all-zones-by-relationship-id")
//	public ResponseEntity<Object> getAllZonesByRelationshipId( @Valid @RequestBody Zones_list_RequestDto zoneDto) {
//		ResponseEntity<Object> responseBuilder = null;
//		try {
//			if (zoneDto.getLinked_zone()!= null) {
//				zones_list_service.getAllZonesByRelationshipId(zoneDto.getLinked_zone());
//				responseBuilder = CustomHttpResponse.responseBuilder("All available Zones_list by relationship id ",
//						HttpStatus.OK, zones_list_service.getAllZonesByRelationshipId(zoneDto.getLinked_zone()));
//			}else {
//				CustomHttpResponse.responseBuilder("Enter request body data ",
//						HttpStatus.BAD_REQUEST, null);
//			}
//		} catch (Exception e) {
//			throw new ResourceNotFoundException("Zones_list ", "relationship id : "+zoneDto.getLinked_zone(), "not found");
//
//		}
//		return responseBuilder;
//
//	}

	@GetMapping("/zones/{zoneId}")
	public ResponseEntity<Object> listZoneById(@PathVariable String zoneId) {
		Zones_list_ResponseDto responseDto = zones_list_service.listZoneById(zoneId);
		return CustomHttpResponse.responseBuilder("Zones_list  with this  zone_id: " + zoneId, HttpStatus.OK,
				responseDto);
	}

	@GetMapping("/zone-by-id/{zoneId}")
	public Object listZoneByZoneId(@PathVariable String zoneId) {
		return zones_list_service.listZoneById(zoneId);

	}

	@PostMapping("/zones")
	public ResponseEntity<Object> createZones(@RequestBody CreateZoneListDTO zoneDto) {
		CreateZoneListDTO zones_list_ResponseDtos = zones_list_service.createZones(zoneDto);
		return CustomHttpResponse.responseBuilder("All available Zones", HttpStatus.OK, zones_list_ResponseDtos);
	}

	@PostMapping("/update-zones")
	public ResponseEntity<Object> updateZone(@RequestBody Zones_list_RequestDto zones_listDto) {
		Zones_list_ResponseDto updateZone = zones_list_service.updateZone(zones_listDto);
		if (updateZone == null) {
			return CustomHttpResponse.responseBuilder(
					"Zone data not found with the given id : " + zones_listDto.getId(), HttpStatus.BAD_REQUEST,
					updateZone);
		}
		if (updateZone.getName().contains("already found")) {
			return CustomHttpResponse.responseBuilder("Zone already found please enter unique zone ",
					HttpStatus.ALREADY_REPORTED, "");
		}
		return CustomHttpResponse.responseBuilder("Zone_list ", HttpStatus.OK, updateZone);
	}

	@DeleteMapping("/zones/{zoneId}")
	public ResponseEntity<Object> deleteZones(@PathVariable String zoneId) {
		zones_list_service.deleteZones(zoneId);
		return CustomHttpResponse.responseBuilder("Zones_list has been deleted with zoneId :" + zoneId, HttpStatus.OK,
				zoneId);
	}

	@DeleteMapping("/clean-All-zone_list")
	public ResponseEntity<Object> cleanAllZonesList() {
		zones_list_service.cleanAllZone_list();
		return CustomHttpResponse.responseBuilder("All Zones_list has been cleared", HttpStatus.OK,
				"/clean-All-zone_list");
	}

	@PostMapping("get-all-zones-by-relationship-id")
	public ResponseEntity<?> getAllZoneListByRelationshipId(@Valid @RequestBody Zones_list_RequestDto zoneDto) {
		JSONObject zones = zones_list_service.getAllZoneListByRelationshipId(zoneDto.getLinked_zone(),
				zoneDto.getParent_zone_list_id());
		Map<String, Object> zonesMap = zones.toMap();
//        return new ResponseEntity<>(zonesMap, HttpStatus.OK);
		return CustomHttpResponse.responseBuilder("All Available Zones by Relationship Id", HttpStatus.OK, zonesMap);
	}

	@PostMapping("/root-zone-list")
	public ResponseEntity<?> getFiltrationZoneList(@Valid @RequestBody ZoneListFiltrationDTO zoneListFiltrationDTO) {
		List<ZoneListFiltrationResponseDTO> availableFiltration = zoneListService
				.getFiltrationZoneList(zoneListFiltrationDTO.getBelongs_to_zone());
//        System.out.println(availableFiltration + "availableFiltration");

		return CustomHttpResponse.responseBuilder("Available data for Filtration of Zone List", HttpStatus.OK,
				availableFiltration);
	}

	@PostMapping("/zone-list-by-relationship-id")
	public ResponseEntity<?> getAllZonesByRelationshipId(
			@Valid @RequestBody AllZonesByRelationshipIdDTO allZonesByRelationshipIdDTO) {
		List<AvailableZone> zonelist = zoneListService
				.getAllListZonesByLinkedZoneList(allZonesByRelationshipIdDTO.getLinked_zone_list_id());
		if (!zonelist.isEmpty())
			return CustomHttpResponse.responseBuilder("All Available Zones by Relationship Id", HttpStatus.OK,
					zonelist);
		else {
			return CustomHttpResponse.responseBuilder("Zones not available", HttpStatus.NOT_FOUND, zonelist);
		}
	}

	@GetMapping("/parent-zone")
	public ResponseEntity<?> getParentZoneList() {
		List<ParentZoneDTO> parentZoneList = zoneListService.getParentZoneList();
		if (parentZoneList == null || parentZoneList.isEmpty())
			return CustomHttpResponse.responseBuilder("Parent Zone Id", HttpStatus.OK, parentZoneList);

		return CustomHttpResponse.responseBuilder("Parent Zone Id", HttpStatus.OK, parentZoneList.get(0));
	}

	@GetMapping("/get-all-zonelist")
	public List<Zones_list_ResponseDto> getAllZonesListForOtherServices(@RequestBody String[] zoneIds) {
		List<Zones_list_ResponseDto> zones_list_ResponseDtos = zones_list_service.getAllZonesList(zoneIds);
		return zones_list_ResponseDtos;

	}

	/*
	 * get cordinator by given phc ids
	 */
//	@PostMapping("/get-cordinatorsby-linked-zoneId")
//	public ResponseEntity<Object> getCordinatorByLinkedZoneId(@RequestBody LinkedZoneIds zoneId) {
//		List<Zones_list> allCordinators = zones_list_service.getCordinatorByLinkedZoneId(zoneId.getLinkedZoneid());
//		List<String> cordinators = new ArrayList<>();
//		for (Zones_list zones_list : allCordinators) {
//			cordinators.add(zones_list.getCordinator());
//		}
//		return CustomHttpResponse.responseBuilder("Cordinators ids", HttpStatus.OK, cordinators);
//	}
	
	@PostMapping("/get-cordinatorsby-linked-zoneId-for-restcall")
	public List<String> getCordinatorByLinkedZoneId(@RequestBody List<String> zoneId) {
		List<Zones_list> allCordinators = zones_list_service.getCordinatorByLinkedZoneId(zoneId);
		List<String> cordinators = new ArrayList<>();
		for (Zones_list zones_list : allCordinators) {
			cordinators.add(zones_list.getCordinator());
		}
		return cordinators;
	}
	
}
