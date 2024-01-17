package co.deepmindz.adminorghierservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.ListSSUserZonesResponseDto;
import co.deepmindz.adminorghierservice.dto.MemberResponseDto;
import co.deepmindz.adminorghierservice.dto.SSResponseDtoForRestCall;
import co.deepmindz.adminorghierservice.dto.SSUserRequestDto;
import co.deepmindz.adminorghierservice.dto.SSUserResponseDto;
import co.deepmindz.adminorghierservice.dto.SSUserUpdateRequestDto;
import co.deepmindz.adminorghierservice.models.SSUser;
import co.deepmindz.adminorghierservice.models.Zones_list;
<<<<<<< HEAD
import jakarta.validation.Valid;
=======
import co.deepmindz.adminorghierservice.utils.Templates.USERSTATUS;
>>>>>>> branch 'main' of https://github.com/SS-Whitelabel/ss-admin-org-hierarchy-service.git

@Service
public interface SSUserService {

	Integer getZonesCountUsingLinkedZones(String parentZoneID);

	ListSSUserZonesResponseDto getAllZoneForSSUser(String role, List<String> parentZoneID);

	SSUserResponseDto createSSUser(SSUserRequestDto createSSUserData, String loginmode);

	List<SSUserResponseDto> getSubordinateRoleSSUsers(String roleID);

	List<SSUserResponseDto> getSupervisorByRelationshipId(String zoneID);

	List<SSUserResponseDto> getAllSSUsers(String userIDorUsername, boolean isfindByUsername);

	List<SSUserResponseDto> getAllSSUsersByCoordinatorId(String coordinatorID);

	List<Zones_list> getSSUserZonewithSubZoneDetails(String ssuserID);

	List<MemberResponseDto> getTeamMemberByZoneId(String zoneId);

	ResponseEntity<Object> updateSSUserasOccupiedorActiveByIds(String[] ssuserids, USERSTATUS status);

	List<SSResponseDtoForRestCall> allSSUserByIds(List<String> list);

	public String updateUsers(String user_id, SSUserUpdateRequestDto updateRequest);

	public boolean appointAsTeamLeads(List<String> user_id);
	
	public String blockAndUnblockUser(String id);
}
