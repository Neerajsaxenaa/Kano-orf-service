package co.deepmindz.adminorghierservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.deepmindz.adminorghierservice.dto.CustomHttpResponse;
import co.deepmindz.adminorghierservice.dto.ZonesRequestDto;
import co.deepmindz.adminorghierservice.dto.ZonesResponseDto;
import co.deepmindz.adminorghierservice.service.ZoneService;
import jakarta.validation.Valid;

/*
 * Author : Neeraj kumar
 */

@RestController
@RequestMapping("/organization")
public class ZoneController {

	@Autowired
	private ZoneService zoneService;

	private Logger logger = LoggerFactory.getLogger(ZoneController.class);

	@GetMapping("/zone/get-all-zones")
	public ResponseEntity<Object> getAllZones() {
		logger.info("ZoneController.class:getAllZones():get-all-zones");
		String allString = "";
		List<ZonesResponseDto> zonesResponseDto = zoneService.getAllZones(allString);
		return CustomHttpResponse.responseBuilder("All Available Zones", HttpStatus.OK, zonesResponseDto);
	}

	@GetMapping("/zone/zone-by-id/{zoneId}")
	public ResponseEntity<Object> zoneById(@PathVariable String zoneId) {
		logger.info("ZoneController.class:zoneById():zoneId", zoneId);
		ZonesResponseDto responseDto = zoneService.zoneById(zoneId);
		if (zoneService.zoneById(zoneId) != null) {
			return CustomHttpResponse.responseBuilder("Zones with zoneId : " + zoneId, HttpStatus.OK, responseDto);
		}
		return CustomHttpResponse.responseBuilder("Zones not found with zoneId  : " + zoneId, HttpStatus.OK,
				responseDto);
	}

	@GetMapping("/zone/zone-by-id")
	public Object zoneByZoneId(@RequestParam String zoneId) {
		logger.info("ZoneController.class:zoneByZoneId () : zoneId", zoneId);
		return zoneService.zoneById(zoneId);
//		if (zoneService.zoneById(zoneId) != null) {
//			return CustomHttpResponse.responseBuilder("Zones with zoneId : " + zoneId, HttpStatus.OK, responseDto);
//		}
//		return CustomHttpResponse.responseBuilder("Zones not found with zoneId  : " + zoneId, HttpStatus.OK,
//				responseDto);
	}

	@PostMapping("/zone/create-zone")
	public ResponseEntity<Object> createZone(@Valid @RequestBody ZonesRequestDto zonesDto) {
		logger.info("ZoneController.class:createZone():zone", zonesDto);
		ZonesResponseDto responseDto = zoneService.createZone(zonesDto);
		return CustomHttpResponse.responseBuilder("Zones has been created", HttpStatus.CREATED, responseDto);
	}

	@PostMapping("/zone/update-zone/{id}")
	public ResponseEntity<Object> updateZone(@PathVariable("id") String id, @RequestBody ZonesResponseDto zonesDto) {
		logger.info("ZoneController.class:updateZone()", zonesDto);
		ZonesResponseDto updateZone = zoneService.updateZone(id, zonesDto);
		if (updateZone != null) {
			return CustomHttpResponse.responseBuilder("Zone Succesfully Updated", HttpStatus.OK, updateZone);
		}
		return CustomHttpResponse.responseBuilder("Zone not found", HttpStatus.NOT_FOUND, updateZone);
	}

	@PostMapping("/zone/delete-zone/{zoneId}")
	public ResponseEntity<Object> deleteZones_list(@PathVariable String zoneId) {
		logger.info("ZoneController.class:deleteZones_list()", zoneId);
//		ZonesResponseDto zoneById = zoneService.zoneById(zoneId);

		if (zoneService.zoneById(zoneId) != null) {
			zoneService.deleteZones_list(zoneId);
			return CustomHttpResponse.responseBuilder("Zones has been deleted with zoneId :" + zoneId, HttpStatus.OK,
					zoneId);
		}
		return CustomHttpResponse.responseBuilder("Zones not found with zoneId :" + zoneId, HttpStatus.NOT_FOUND,
				zoneId);

	}

	@PostMapping("/clean-All-zone")
	public ResponseEntity<Object> cleanAllZones() {
		logger.info("ZoneController.class:cleanAllZones()");
		zoneService.cleanAllZone();
		return CustomHttpResponse.responseBuilder("All Zones has been cleared", HttpStatus.OK, "/clean-All-zone");
	}

}