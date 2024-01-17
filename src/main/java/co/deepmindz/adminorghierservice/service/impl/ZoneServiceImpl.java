package co.deepmindz.adminorghierservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.deepmindz.adminorghierservice.dto.ZonesRequestDto;
import co.deepmindz.adminorghierservice.dto.ZonesResponseDto;
import co.deepmindz.adminorghierservice.models.Roles;
import co.deepmindz.adminorghierservice.models.Zones;
import co.deepmindz.adminorghierservice.repository.RolesRepository;
import co.deepmindz.adminorghierservice.repository.ZoneRepo;
import co.deepmindz.adminorghierservice.service.ZoneService;
import co.deepmindz.adminorghierservice.utils.ZonesUtil;
import jakarta.validation.Valid;

@Service
public class ZoneServiceImpl implements ZoneService {

	@Autowired
	private ZonesUtil zonesUtil;

	@Autowired
	private ZoneRepo zoneRepo;

	@Autowired
	private RolesRepository rolesRepository;

	@Override
	public ZonesResponseDto createZone(@Valid ZonesRequestDto zonesDto) {
		Zones zones = zonesUtil.mapRequestDtoToEntity(zonesDto);
		Roles roles = new Roles();
		String supervisorRoleIdString = rolesRepository.getSupervisorRoleId(zonesDto.getParentZone_id());
		roles.setTitle(zonesDto.getRole().toUpperCase());
		if (supervisorRoleIdString != null) {
			Zones savedZone = zoneRepo.save(zones);
			roles.setSupervisorRole_id(supervisorRoleIdString);
			roles.setZone_id(savedZone.getZone_id());
			rolesRepository.save(roles);
		} else {
			Zones savedZone = zoneRepo.save(zones);
			roles.setSupervisorRole_id(null);
			roles.setZone_id(savedZone.getZone_id());
			rolesRepository.save(roles);
		}
		return zonesUtil.mapEntityToResponseDto(List.of(zones)).get(0);

	}

	@Override
	public List<ZonesResponseDto> getAllZones(String ZonesId) {
		List<ZonesResponseDto> zonesResponseDto = new ArrayList<>();
		if (!ZonesId.isEmpty()) {
			zonesResponseDto.addAll(zonesUtil.mapEntityToResponseDto(List.of(zoneRepo.findById(ZonesId).get())));
		} else {
			zonesResponseDto.addAll(
					zonesUtil.mapEntityToResponseDto(zoneRepo.getZonesInHierarchy()));
//			zonesResponseDto.addAll(
//					zonesUtil.mapEntityToResponseDto(zoneRepo.findAll(Sort.by(Sort.Direction.ASC, "createdat"))));
		}
		return zonesResponseDto;
	}

	@Override
	public ZonesResponseDto zoneById(String zoneId) {
		Optional<Zones> zones = zoneRepo.findById(zoneId);
		if (zones.isPresent()) {
			return zonesUtil.mapEntityToResponseDto(zones);
		}
		return null;
	}

	@Override
	public ZonesResponseDto updateZone(String id, ZonesResponseDto zonesDto) {
		Zones zone = new Zones();
		Zones savedZone = null;
		Optional<Zones> zones = zoneRepo.findById(id);
		if (zones.isPresent()) {
			zone.setZone_id(zones.get().getZone_id());
			zone.setCreated_at(zones.get().getCreated_at());
			zone.setName(zonesDto.getName().toUpperCase());
			zone.setParentZone_id(zones.get().getParentZone_id());
			zone.setZone_code(zones.get().getZone_code());
			savedZone = zoneRepo.save(zone);
			return zonesUtil.mapEntityToResponseDto(savedZone);
		}
		return null;

	}

	@Override
	public ZonesResponseDto deleteZones_list(String zoneId) {
		ZonesResponseDto zonesResponseDto = null;
//		zoneRepo.deleteById(zoneId);
		if (zoneRepo.findById(zoneId) != null) {
			zoneRepo.deleteById(zoneId);
			return zonesResponseDto;
		}
		return null;
	}

	@Override
	public List<ZonesResponseDto> getZonesHierarchy() {
		return zonesUtil.mapEntityToResponseDto(zoneRepo.getZonesInHierarchy());
//		return zonesUtil.mapEntityToResponseDto(zoneRepo.findAll(Sort.by(Sort.Direction.ASC, "createdat")));
	}

	@Override
	public void cleanAllZone() {
		zoneRepo.deleteAll();
	}
}
