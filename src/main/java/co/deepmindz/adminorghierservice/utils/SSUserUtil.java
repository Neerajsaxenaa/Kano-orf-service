package co.deepmindz.adminorghierservice.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.MemberResponseDto;
import co.deepmindz.adminorghierservice.dto.SSResponseDtoForRestCall;
import co.deepmindz.adminorghierservice.dto.SSUserRequestDto;
import co.deepmindz.adminorghierservice.dto.SSUserResponseDto;
import co.deepmindz.adminorghierservice.models.Roles;
import co.deepmindz.adminorghierservice.models.SSUser;
import co.deepmindz.adminorghierservice.repository.RolesRepository;
import co.deepmindz.adminorghierservice.repository.SSUserRepository;
import co.deepmindz.adminorghierservice.utils.CustomDataTypes.SupervisorOfSSUSer;

@Service
public class SSUserUtil {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	SSUserRepository ssUserRepository;

	public SSUser mapRequestDtoToEntity(SSUserRequestDto ssUserDto, String loginmode) {

		String immediateLinkedZone = null;
		SSUser ssUser = new SSUser();
		ssUser.setUsername(ssUserDto.getUserName());
		ssUser.setRole_id(ssUserDto.getRole());
		List<String> linkedParentZones = new ArrayList<>();
		for (int i = 0; i < ssUserDto.getLinked_zones().length; i++) {
			if (i == ssUserDto.getLinked_zones().length - 1) {
				immediateLinkedZone = ssUserDto.getLinked_zones()[i].getZone_id();
			}

			linkedParentZones
					.add(ssUserDto.getLinked_zones()[i].getZone() + ":" + ssUserDto.getLinked_zones()[i].getZone_id());
		}
		ssUser.setLinkedParentZones(linkedParentZones.toArray(new String[linkedParentZones.size()]));
		ssUser.setLinkedZone(immediateLinkedZone);
		ssUser.setStatus(Templates.USERSTATUS.ACTIVE.name());
		ssUser.setPhoneNumber(ssUserDto.getPhoneNumber());
		if (loginmode.equals(Templates.LOGINMODES.USER_CREDENTIALS.name()))
			ssUser.setPassword(passwordEncoder.encode(ssUserDto.getPassword()));

		List<String> supervisors = new ArrayList<>();
		for (SupervisorOfSSUSer obj : ssUserDto.getLinked_supervisors())
			supervisors.add(obj.supervisor_id);

		ssUser.setLinkedSupervisors(supervisors.toArray(new String[supervisors.size()]));
		return ssUser;
	}

	public SSUserResponseDto mapEntityToResponseDto(SSUser user) {
		List<Roles> allRoles = rolesRepository.findAll();
		Map<String, String> idWithRoleNameMap = new HashMap<>();
		for (Roles roles : allRoles)
			idWithRoleNameMap.put(roles.getRole_id(), roles.getTitle());

		List<String> allLinkedZoneNames = new ArrayList<>();
		for (String zone : user.getLinkedParentZones()) {
			allLinkedZoneNames.add(zone.split(":")[0]);
		}
		Collections.reverse(allLinkedZoneNames);

		List<SSUser> supervisors = ssUserRepository.findAllById(List.of(user.getLinkedSupervisors()));
		Map<String, String> idWithSSUserNameMap = new HashMap<>();
		for (SSUser user2 : supervisors)
			idWithSSUserNameMap.put(user2.getUser_id(), user2.getUsername());

		List<String> supervisorslist = new ArrayList<>();
		for (String supervisor : user.getLinkedSupervisors()) {
			supervisorslist.add(idWithSSUserNameMap.get(supervisor));
			supervisorslist.add(supervisor);
		}

		return new SSUserResponseDto(user.getUser_id(), idWithRoleNameMap.get(user.getRole_id()), user.getUsername(),
				user.getPhoneNumber(), allLinkedZoneNames.toArray(new String[allLinkedZoneNames.size()]),
				supervisorslist.toArray(new String[supervisorslist.size()]), user.getCreated_at());
	}

	public MemberResponseDto mapEntityToMemberResponseDto(SSUser user) {
		List<Roles> allRoles = rolesRepository.findAll();
		Map<String, String> idWithRoleNameMap = new HashMap<>();
		for (Roles roles : allRoles)
			idWithRoleNameMap.put(roles.getRole_id(), roles.getTitle());

		List<String> allLinkedZoneNames = new ArrayList<>();
		for (String zone : user.getLinkedParentZones()) {
			allLinkedZoneNames.add(zone.split(":")[0]);
		}
//		Collections.reverse(allLinkedZoneNames);

		List<SSUser> supervisors = ssUserRepository.findAllById(List.of(user.getLinkedSupervisors()));
		Map<String, String> idWithSSUserNameMap = new HashMap<>();
		for (SSUser user2 : supervisors)
			idWithSSUserNameMap.put(user2.getUser_id(), user2.getUsername());

		List<String> supervisorslist = new ArrayList<>();
		for (String supervisor : user.getLinkedSupervisors()) {
			supervisorslist.add(idWithSSUserNameMap.get(supervisor));
//			supervisorslist.add(supervisor);
		}

		return new MemberResponseDto(user.getUser_id(), user.getUsername(), idWithRoleNameMap.get(user.getRole_id()),
				user.getUsername(), user.getStatus(), allLinkedZoneNames.toArray(new String[allLinkedZoneNames.size()]),

				user.getCreated_at());
	}

	public SSUserResponseDto mapEntityToResponseDtoForAllSSUser(SSUser user, Map<String, String> idWithSSUserNameMap,
			Map<String, String> idWithRoleNameMap) {

		List<String> allLinkedZoneNames = new ArrayList<>();
		for (String zone : user.getLinkedParentZones()) {
			allLinkedZoneNames.add(zone.split(":")[0]);
		}
		Collections.reverse(allLinkedZoneNames);

		List<String> supervisors = new ArrayList<>();
		for (String supervisor : user.getLinkedSupervisors()) {
			supervisors.add(idWithSSUserNameMap.get(supervisor));
			supervisors.add(supervisor);
		}

		return new SSUserResponseDto(user.getUser_id(), idWithRoleNameMap.get(user.getRole_id()), user.getUsername(),
				user.getPhoneNumber(), allLinkedZoneNames.toArray(new String[allLinkedZoneNames.size()]),
				supervisors.toArray(new String[supervisors.size()]), user.getCreated_at());
	}

	public List<SSResponseDtoForRestCall> mapListOfSSUserToListOfSSResponse(List<SSUser> findAllById) {
		List<SSResponseDtoForRestCall> ssResponseDtoForRestCallList = new ArrayList<>();
		for (SSUser ssuser : findAllById) {
			SSResponseDtoForRestCall ssResponseDtoForRestCall = new SSResponseDtoForRestCall();
			ssResponseDtoForRestCall.setMember_id(ssuser.getUser_id());
//			ssResponseDtoForRestCall.setMember_name(ssuser.getName());
			ssResponseDtoForRestCall.setPhone_number(ssuser.getPhoneNumber());
			ssResponseDtoForRestCall.setUser_name(ssuser.getUsername());
			ssResponseDtoForRestCall.setDesignation(ssuser.getRole_id());
			ssResponseDtoForRestCall.setLinkedZones(ssuser.getLinkedZone());
			ssResponseDtoForRestCall.setStatus(ssuser.getStatus());
			ssResponseDtoForRestCall.setCreated_at(ssuser.getCreated_at());
			ssResponseDtoForRestCallList.add(ssResponseDtoForRestCall);
		}
		return ssResponseDtoForRestCallList;
	}
}
