package co.deepmindz.adminorghierservice.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import co.deepmindz.adminorghierservice.dto.ConfigManagementRequestDto;
import co.deepmindz.adminorghierservice.dto.MemberResponseDto;
import co.deepmindz.adminorghierservice.dto.SSResponseDtoForRestCall;
import co.deepmindz.adminorghierservice.dto.SSUserRequestDto;
import co.deepmindz.adminorghierservice.dto.SSUserResponseDto;
import co.deepmindz.adminorghierservice.dto.SSUserUpdateRequestDto;
import co.deepmindz.adminorghierservice.exception.ResourceAlreadyExist;
import co.deepmindz.adminorghierservice.exception.ResourceNotFoundException;
import co.deepmindz.adminorghierservice.resources.CustomHttpResponse;
import co.deepmindz.adminorghierservice.service.RolesService;
import co.deepmindz.adminorghierservice.service.SSUserService;
import co.deepmindz.adminorghierservice.service.ZoneService;
import co.deepmindz.adminorghierservice.service.Zones_list_service;
import co.deepmindz.adminorghierservice.utils.Templates;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ss-user")
public class SSUserController {

	@Autowired
	RolesService rolesService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	SSUserService ssUserService;

	@Autowired
	ZoneService zoneService;

	@Autowired
	Zones_list_service zones_list_service;

	private Map<String, String> loginmode = null;

	private Logger logger = LoggerFactory.getLogger(SSUserController.class);

	private static ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
	};

//	@GetMapping("/all-roles")
//	public ResponseEntity<Object> getAllRoles() {
//		logger.info("ListSSUser.class:getAllRoles():all-roles");
//		List<RolesResponseDto> allRoles = rolesService.getAllRoles();
//		if (loginmode == null) {
//			RequestEntity<Void> request = RequestEntity.get(services[0] + "/loginmode/current-loginMode-status")
//					.accept(MediaType.APPLICATION_JSON).build();
//			loginmode = restTemplate.exchange(request, responseType).getBody();
//		}
//		Map<String, Object> response = new HashMap<>();
//		response.put("allRoles", allRoles);
//		response.put("loginmode", loginmode);
//		logger.info("ListSSUser.class:getAllRoles():all-roles", response);
//		return CustomHttpResponse.responseBuilder("All Roles", HttpStatus.OK, response);
//	}

//	@PostMapping("/get-all-zones")
//	public ResponseEntity<Object> getZonesDetails(@RequestBody ListSSUserZonesRequestDto parentid_role) {
//		logger.info("ListSSUser.class:getZoneDetails():get-all-zones", parentid_role);
//		return CustomHttpResponse.responseBuilder("Zones for " + parentid_role.getRole(), HttpStatus.OK,
//				ssUserService.getAllZoneForSSUser(parentid_role.getRole(), List.of(parentid_role.getParent_id())));
//	}

//	@PostMapping("/get-all-subzones")
//	public ResponseEntity<Object> getSubZonesDetails(@RequestBody ListSSUserSubZonesRequestDto subZoneTypeDetails) {
//		logger.info("ListSSUser.class:getSubZonesDetails():get-all-subzones", subZoneTypeDetails);
//		List<ZonesResponseDto> allZones = zoneService.getZonesHierarchy();
//		Map<String, String> zonesHierarchyMap = new HashMap<>();
//
//		for (int i = 0; i < allZones.size() - 1; i++) {
//			zonesHierarchyMap.put(allZones.get(i).getName(), allZones.get(i + 1).getName());
//		}
//		if (ssUserService.getZonesCountUsingLinkedZones(subZoneTypeDetails.getZone_id()) <= 0) {
//			logger.error("ListSSUser.class:getSubZonesDetails():get-all-subzones", "ResourceNotFoundException");
//			throw new ResourceNotFoundException("zone_list", "any ID", "");
//		}
//		ListSSUserSubZonesResponseDto subZoneData = new ListSSUserSubZonesResponseDto();
//		subZoneData.setItems(ssUserService.getSubZonesLevelDetails(subZoneTypeDetails));
//		subZoneData.setSub_zone(zonesHierarchyMap.get(subZoneTypeDetails.getType()));
//		logger.info("ListSSUser.class:getSubZonesDetails():get-all-subzones", subZoneData);
//		return CustomHttpResponse.responseBuilder("items for " + subZoneTypeDetails.getType(), HttpStatus.OK,
//				subZoneData);
//	}

	@PostMapping("/add-ssuser")
	public ResponseEntity<Object> createSSUSer(@Valid @RequestBody SSUserRequestDto createSSUserData)
			throws OperationNotSupportedException {
		logger.info("SSUser.class : createSSUser() : " + createSSUserData);
		RequestEntity<Void> request = RequestEntity
				.get(Templates.ALLSERVICES.admin_main.toString()
						+ "/admin-main/external-resource/check-user-base-resource")
				.accept(MediaType.APPLICATION_JSON).build();
		if (restTemplate.exchange(request, responseType).getBody().get("data") == "true") {
			throw new OperationNotSupportedException(
					"HRM based configuration found, can't create new user." + " please contact your HRM Team");
		}
		request = RequestEntity
				.get(Templates.ALLSERVICES.admin_main.toString() + "/admin-main/login-mode/current-loginMode-status")
				.accept(MediaType.APPLICATION_JSON).build();
		loginmode = restTemplate.exchange(request, responseType).getBody();

		if (loginmode.get("data").equals(Templates.LOGINMODES.Two_FA.name())) {
			if (createSSUserData.getPhoneNumber().isEmpty() || createSSUserData.getPhoneNumber().isBlank())
				throw new OperationNotSupportedException("Please provide valid phone number");
			else {
				List<SSUserResponseDto> foundElement = ssUserService.getAllSSUsers(createSSUserData.getPhoneNumber(),
						true);
				if (foundElement != null)
					throw new ResourceAlreadyExist("Phone Number already registered");
			}
		} else if (loginmode.get("data").equals(Templates.LOGINMODES.USER_CREDENTIALS.name())) {
			List<SSUserResponseDto> foundElement = ssUserService.getAllSSUsers(createSSUserData.getUserName(), true);
			if (foundElement != null)
				throw new ResourceAlreadyExist("User already exists with given user");
		} else {
			throw new OperationNotSupportedException("Loginmode is not configured");
		}
		SSUserResponseDto createdUser = ssUserService.createSSUser(createSSUserData, loginmode.get("data"));
		return CustomHttpResponse.responseBuilder("SSUser has been created successfully", HttpStatus.CREATED,
				createdUser);
	}

	// return the sub-ordinates of this ssuser
	@GetMapping("/get-subordinate-by-relationship-id")
	public Object getSubOrdinateRoles(@RequestParam String ssUserID) {
		List<SSUserResponseDto> subordinateRoleSSUsers = ssUserService.getSubordinateRoleSSUsers(ssUserID);
		return subordinateRoleSSUsers;
	}

	/*
	 * will be called In Teams mode only These members will be used in ISS Team
	 * creation They are not subordinates, they are members from same zone.
	 */
	@GetMapping("/members-by-relationship-id")
	public ResponseEntity<Object> getTeamMemberByZoneId(@RequestParam String zoneId) {
		List<MemberResponseDto> teamMemberByZoneId = ssUserService.getTeamMemberByZoneId(zoneId);
		if (teamMemberByZoneId == null) {
			return CustomHttpResponse.responseBuilder("No Team member found in this zone..!!", HttpStatus.OK,
					teamMemberByZoneId);
		}
		return CustomHttpResponse.responseBuilder("All members in this zone..!!", HttpStatus.OK, teamMemberByZoneId);
	}

//	@GetMapping("/members-by-relationship-id-forRestyCall")
//	public Object getTeamMemberByZoneId(@RequestParam String zoneId) {
//		 List<MemberResponseDto> teamMemberByZoneId = ssUserService.getTeamMemberByZoneId(zoneId);
//		 if (teamMemberByZoneId==null) {
//			 return CustomHttpResponse.responseBuilder("No Team member found in this zone..!!", HttpStatus.OK, "");
//		}
//		 return  teamMemberByZoneId;
//	}

	// return the supervisor of
	@GetMapping("/get-user-by-zone-id")
	public ResponseEntity<Object> getSupervisorByRelationshipId(@Valid @RequestParam String zoneId) {
		List<SSUserResponseDto> supervisors = ssUserService.getSupervisorByRelationshipId(zoneId);
		return CustomHttpResponse.responseBuilder("Supervisors by Relationships :", HttpStatus.OK, supervisors);
	}

	@GetMapping("/get-ssuser-byusername")
	public ResponseEntity<Object> getSSUserDetailsForExternalService(@RequestParam String username) {
		return CustomHttpResponse.responseBuilder("Details of SSUser  ", HttpStatus.OK,
				ssUserService.getAllSSUsers(username, true));
	}

	@GetMapping("/get-all-ssusers")
	public ResponseEntity<Object> getAllSSUsers() {
		return CustomHttpResponse.responseBuilder("AllSSUser ", HttpStatus.OK,
				ssUserService.getAllSSUsers(null, false));
	}

	@GetMapping("/get-ssuser-byuserid")
	public ResponseEntity<Object> getUserDetailsByUserId(@RequestParam String userid) {
		return CustomHttpResponse.responseBuilder("SSUser Details ", HttpStatus.OK,
				ssUserService.getAllSSUsers(userid, false));
	}

	@GetMapping("/get-user-all-zonedetails")
	public ResponseEntity<Object> getSSUserZonewithSubZoneDetails(@RequestParam String userid) {
		return CustomHttpResponse.responseBuilder("SSUser Details : ", HttpStatus.OK,
				ssUserService.getSSUserZonewithSubZoneDetails(userid));
	}

	@PostMapping("/update-by-ssuser-ids")
	public Object updateUserByIds(@RequestBody String[] ssuserids) {
		return ssUserService.updateUserByIds(ssuserids);

	}

	@PostMapping("/all-ssuser-by-ids-forRestcall")
	public List<SSResponseDtoForRestCall> allSSUserByIds(@RequestBody String[] ssuserids) {
		return ssUserService.allSSUserByIds(Arrays.asList(ssuserids));

	}

	@PostMapping("/set-configuration")
	public Object setConfiguration(@Valid @RequestBody ConfigManagementRequestDto dto) {
		try {
			String freezeApi = Templates.ALLSERVICES.admin_main.toString()
					+ "/admin-main/config-management/set-configuration";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> entity = new HttpEntity<Object>(dto, headers);
			Object postForObject = restTemplate.postForObject(freezeApi, entity, Object.class);
			return postForObject;
		} catch (Exception e) {
			System.out.println("freezeApi Api not working..!!");
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/get-configuration")
	public Object getConfiguration(@Valid @RequestBody ConfigManagementRequestDto dto) {
		try {
			String getConfigurationForfreezeApi = Templates.ALLSERVICES.admin_main.toString()
					+ "/admin-main/config-management/get-configuration";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> entity = new HttpEntity<Object>(dto, headers);
			return restTemplate.postForObject(getConfigurationForfreezeApi, entity, Object.class);

		} catch (Exception e) {
			System.out.println("getConfigurationForfreezeApi Api not working ");
			e.printStackTrace();
			return restTemplate;
		}
	}

	@GetMapping("/get-all-configuration")
	public Object getAllConfiguration() {
		logger.info("SSUserController.class:getAllConfiguration: get-all-configuration");
		try {
			String getAllFreezeConfigurationApi = Templates.ALLSERVICES.admin_main.toString()
					+ "/admin-main/config-management/get-all-configuration";
			return restTemplate.getForObject(getAllFreezeConfigurationApi, Object.class);
		} catch (Exception e) {
			e.printStackTrace();
			return CustomHttpResponse.responseBuilder("getAllFreezeConfigurationApi  is not working..!!",
					HttpStatus.NOT_FOUND, null);
		}
	}

	@PostMapping("/update_ssuser_phone/{username}")
	public ResponseEntity<Object> updateSSUserPhone(@PathVariable String username,
			@Valid @RequestBody SSUserUpdateRequestDto updateRequest) {
		String response = ssUserService.updateUsers(username, updateRequest);
		if (response == null)
			throw new ResourceNotFoundException("SSUSER", username, username);
		return CustomHttpResponse.responseBuilder("SSUser has been updated", HttpStatus.OK, response);
	}

	@PostMapping("/reset_ssuser_password/{username}")
	public ResponseEntity<Object> resetSSUserPassword(@PathVariable String username,
			@Valid @RequestBody SSUserUpdateRequestDto updateRequest) {
		String response = ssUserService.updateUsers(username, updateRequest);
		if (response == null)
			throw new ResourceNotFoundException("SSUSER", username, username);
		return CustomHttpResponse.responseBuilder("SSUser has been updated", HttpStatus.OK, response);
	}
}
