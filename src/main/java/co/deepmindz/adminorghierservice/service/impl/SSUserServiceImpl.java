package co.deepmindz.adminorghierservice.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.squareup.okhttp.OkHttpClient;

import co.deepmindz.adminorghierservice.dto.ListSSUserZonesResponseDto;
import co.deepmindz.adminorghierservice.dto.MemberResponseDto;
import co.deepmindz.adminorghierservice.dto.SSResponseDtoForRestCall;
import co.deepmindz.adminorghierservice.dto.SSUserRequestDto;
import co.deepmindz.adminorghierservice.dto.SSUserResponseDto;
import co.deepmindz.adminorghierservice.dto.SSUserUpdateRequestDto;
import co.deepmindz.adminorghierservice.models.SSUser;
import co.deepmindz.adminorghierservice.models.Zones_list;
import co.deepmindz.adminorghierservice.repository.RolesRepository;
import co.deepmindz.adminorghierservice.repository.SSUserRepository;
import co.deepmindz.adminorghierservice.repository.Zones_list_Repo;
import co.deepmindz.adminorghierservice.resources.CustomHttpResponse;
import co.deepmindz.adminorghierservice.service.SSUserService;
import co.deepmindz.adminorghierservice.utils.SSUserUtil;
import co.deepmindz.adminorghierservice.utils.Templates;
import co.deepmindz.adminorghierservice.utils.Zones_list_util;
import jakarta.validation.Valid;

@Service
public class SSUserServiceImpl implements SSUserService {

	@Autowired
	Zones_list_Repo zones_list_Repo;

	@Autowired
	Zones_list_util zones_list_util;

	@Autowired
	SSUserRepository ssUserRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	SSUserUtil ssUserUtil;

	public Integer getZonesCountUsingLinkedZones(String parentZoneID) {
		return zones_list_Repo.getZonesUsingLinkedZonesUsingInClause(List.of(parentZoneID)).size();

	}

	@Override
	public ListSSUserZonesResponseDto getAllZoneForSSUser(String role, List<String> parentZoneID) {
		List<Zones_list> zones = zones_list_Repo.getZonesUsingLinkedZonesUsingInClause(parentZoneID);
		List<String> subZonesIds = new ArrayList<>();
		for (Zones_list zone : zones)
			subZonesIds.add(zone.get_id());

		List<Zones_list> subZones = zones_list_Repo.getZonesUsingLinkedZonesUsingInClause(subZonesIds);

		return zones_list_util.prepareZoneWithSubZonesResponse(role, zones, subZones);

	}

	public SSUserResponseDto createSSUser(SSUserRequestDto ssUserDto, String loginmode) {
		SSUser user = ssUserUtil.mapRequestDtoToEntity(ssUserDto, loginmode);
		SSUser createdUser = ssUserRepository.save(user);

		return new SSUserResponseDto(createdUser.getUser_id(), createdUser.getRole_id(), createdUser.getUsername(),
				createdUser.getPhoneNumber(), createdUser.getLinkedParentZones(), createdUser.getLinkedSupervisors(),
				createdUser.getCreated_at());
	}

//	public List<Zones_list_ResponseDto> getSubZonesLevelDetails(ListSSUserSubZonesRequestDto dto) {
//		List<Zones_list> zones = zones_list_Repo.getZonesUsingLinkedZonesUsingInClause(List.of(dto.getZone_id()));
//
//		return zones_list_util.mapEntityToResponseDto(zones);
//	}

	public List<SSUserResponseDto> getSubordinateRoleSSUsers(String roleID) {
		List<SSUser> subUsers = ssUserRepository.findByLinkedSupervisors(new String[] { roleID });
		List<SSUserResponseDto> response = new ArrayList<>();
		for (SSUser user : subUsers)
			response.add(ssUserUtil.mapEntityToResponseDto(user));
		return response;
	}

	public List<SSUserResponseDto> getSupervisorByRelationshipId(String zoneId) {

		List<SSUser> users = ssUserRepository.findByLinkedZone(zoneId);
		if (users.isEmpty())
			return null;

		List<SSUserResponseDto> response = new ArrayList<>();

		for (SSUser user : users) {
			response.add(ssUserUtil.mapEntityToResponseDto(user));
		}
		return response;
	}

	public List<SSUserResponseDto> getAllSSUsers(String userIDorUsername, boolean isfindByUsername) {
		List<SSUser> allUsers = new ArrayList<>();
		if (userIDorUsername == null)
			allUsers = ssUserRepository.findAll();
		else if (isfindByUsername) {
			SSUser user = ssUserRepository.findByUsername(userIDorUsername);
			if (user != null)
				allUsers.add(user);
		} else if (!isfindByUsername) {
			Optional<SSUser> user = ssUserRepository.findById(userIDorUsername);
			if (!user.isEmpty())
				allUsers.add(user.get());
		}
		if (allUsers.isEmpty() || allUsers.size() == 0)
			return null;

		List<SSUserResponseDto> responseList = new ArrayList<>();
		for (SSUser user : allUsers) {
			List<SSUser> supervisors = ssUserRepository.findAllById(List.of(user.getLinkedSupervisors()));
			Map<String, String> idWithSSUserNameMap = new HashMap<>();
			for (SSUser user2 : supervisors)
				idWithSSUserNameMap.put(user2.getUser_id(), user2.getUsername());
			responseList.add(ssUserUtil.mapEntityToResponseDtoForAllSSUser(user, idWithSSUserNameMap));
		}
		return responseList;
	}

	public List<Zones_list> getSSUserZonewithSubZoneDetails(String ssuserID) {
		Optional<SSUser> user = ssUserRepository.findById(ssuserID);
		if (user.isEmpty())
			return null;
		List<String> allZones = new ArrayList<>();
		for (String zone : user.get().getLinkedParentZones())
			allZones.add(zone.split(":")[0]);

		List<Zones_list> zoneList = ssUserRepository.getUserAllZonesDetails(allZones);
		System.out.println(zoneList);
		return zoneList;
	}

	@Override
	public List<MemberResponseDto> getTeamMemberByZoneId(String zoneId) {
		List<SSUser> teamMemberList = ssUserRepository.getTeamMemberByZoneId(zoneId);
		List<MemberResponseDto> response = new ArrayList<>();
		if (teamMemberList.isEmpty() || teamMemberList == null) {
			return response;
		}
		for (SSUser user : teamMemberList)
			response.add(ssUserUtil.mapEntityToMemberResponseDto(user));
		return response;
	}

	@Override
	public ResponseEntity<Object> updateUserByIds(String[] memberIds) {
		List<SSUser> findByIds = ssUserRepository.findByIds(memberIds);
		if (findByIds.isEmpty() || findByIds == null) {
			return CustomHttpResponse.responseBuilder("Member details not found ", HttpStatus.NOT_FOUND, findByIds);
		}
		for (SSUser ssuser : findByIds) {
			String status = ssuser.getStatus();
			if (status.equals(Templates.USERSTATUS.OCCUPIED.name())) {
				return CustomHttpResponse.responseBuilder("Member already occupied , please choose active member..!!",
						HttpStatus.IM_USED, "Member occupied..!!");
			}
			ssuser.setStatus(Templates.USERSTATUS.OCCUPIED.name());
			ssUserRepository.save(ssuser);

		}
		return CustomHttpResponse.responseBuilder("Member details", HttpStatus.OK, findByIds);
	}

	@Override
	public List<SSResponseDtoForRestCall> allSSUserByIds(List<String> ssuserids) {
		List<SSUser> findAllById = ssUserRepository.findAllById(ssuserids);
		return ssUserUtil.mapListOfSSUserToListOfSSResponse(findAllById);
	}

	@Override
	public String updateUsers(String username, SSUserUpdateRequestDto updateRequest) {
		SSUser user = ssUserRepository.findByUsername(username);
		String message = "";
		if (user == null)
			return null;
		if (updateRequest.getPhoneNumber() != null && !updateRequest.getPhoneNumber().isEmpty()) {
			user.setPhoneNumber(updateRequest.getPhoneNumber());
			message = "Phone updated successfully";
		} else {
			user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
			message = "Password reset successfully";
		}
		SSUser savedUser = ssUserRepository.save(user);
		if (savedUser == null)
			message = "Some error occured";
		return message;
	}

	@Override
	public SSUser getSSUserById(@Valid String userid) throws Exception {
		SSUser ssusers = ssUserRepository.findById(userid).get();
		if (ssusers == null)
			throw new Exception("User not found..!!");
		return ssusers;
	}

}
