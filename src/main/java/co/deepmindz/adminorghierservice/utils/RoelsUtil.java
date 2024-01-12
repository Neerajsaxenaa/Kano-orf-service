package co.deepmindz.adminorghierservice.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.RolesRequestDto;
import co.deepmindz.adminorghierservice.dto.RolesResponseDto;
import co.deepmindz.adminorghierservice.models.Roles;

@Service
public class RoelsUtil {

	public Roles mapRequestDtoToEntity(RolesRequestDto dto) {
		Roles newManager = new Roles();
		newManager.setTitle(dto.getTitle().toUpperCase());
		newManager.setZone_id(dto.getZone_id());
		newManager.setSupervisorRole_id(dto.getSupervisorRole_id());
		newManager.setRole_code(dto.getRole_code());

		return newManager;
	}

	public List<RolesResponseDto> mapEntityToResposeDto(List<Roles> managers, String managerID,
			boolean isCreateManager) {
		List<RolesResponseDto> listOfDto = new ArrayList<>();
		if (isCreateManager) {
			listOfDto.add(new RolesResponseDto(managers.get(0).getRole_id(), managers.get(0).getTitle(),
					managers.get(0).getRole_code(), managers.get(0).getZone_id(),
					managers.get(0).getSupervisorRole_id(), managers.get(0).getCreated_at(), null));
		} else {
			Roles manager = managers.get(0);
			Roles supervisor = managers.get(1);
			if (managers.get(0).getRole_id().equals(managerID)) {
				manager = managers.get(0);
				supervisor = managers.get(1);
			} else {
				manager = managers.get(1);
				supervisor = managers.get(0);
			}

			RolesResponseDto supervisorDto = new RolesResponseDto(supervisor.getRole_id(), supervisor.getTitle(),
					supervisor.getRole_code(), supervisor.getZone_id(), supervisor.getSupervisorRole_id(),
					supervisor.getCreated_at(), null);
			if (supervisorDto.getRole_id() != null && !supervisorDto.getRole_id().equals("null")) {
				listOfDto.add(new RolesResponseDto(manager.getRole_id(), manager.getTitle(), manager.getRole_code(),
						manager.getZone_id(), manager.getSupervisorRole_id(), manager.getCreated_at(), supervisorDto));
			} else {
				listOfDto.add(new RolesResponseDto(manager.getRole_id(), manager.getTitle(), manager.getRole_code(),
						manager.getZone_id(), manager.getSupervisorRole_id(), manager.getCreated_at(), null));
			}
		}
		return listOfDto;
	}

	public List<RolesResponseDto> MapEntityToWithoutSuperVisorDetailsResposeDto(List<Roles> allManagers) {
		List<RolesResponseDto> allManagersRepose = new ArrayList<>();
		for (Roles manager : allManagers) {
			RolesResponseDto dto = new RolesResponseDto(manager.getRole_id(), manager.getTitle(),
					manager.getRole_code(), manager.getZone_id(), manager.getSupervisorRole_id(),
					manager.getCreated_at(), null);

			allManagersRepose.add(dto);
		}
		return allManagersRepose;
	}

	public Timestamp parseStringToTimestamp(String timeString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Timestamp timestamp = null;
		Date parsedDate;
		try {
			parsedDate = dateFormat.parse(timeString);
			timestamp = new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	public List<Roles> generateManagerWithSupervisor(String allProperties) {
		List<Roles> allManagers = new ArrayList<>();
		Roles supervisor = new Roles();
		Roles manager = new Roles();

		String[] tokens = allProperties.split(",");
		supervisor.setRole_id(tokens[0]);
		supervisor.setCreated_at(parseStringToTimestamp(tokens[1]));
		supervisor.setRole_code(tokens[2]);
		supervisor.setSupervisorRole_id(tokens[3]);
		supervisor.setTitle(tokens[4]);
		supervisor.setZone_id(tokens[5]);
		allManagers.add(supervisor);

		manager.setRole_id(tokens[6]);
		manager.setCreated_at(parseStringToTimestamp(tokens[7]));
		manager.setRole_code(tokens[8]);
		manager.setSupervisorRole_id(tokens[9]);
		manager.setTitle(tokens[10]);
		manager.setZone_id(tokens[11]);
		allManagers.add(manager);

		return allManagers;
	}
}
