package co.deepmindz.adminorghierservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.deepmindz.adminorghierservice.models.Zones;

public interface ZoneRepo extends JpaRepository<Zones, String> {

	@Query(nativeQuery = true, value = "WITH RECURSIVE zones_hierarchy AS ( SELECT zone_id, created_at, name, "
			+ "parent_zone_id, zone_code FROM Zones WHERE parent_zone_id is null "
			+ "UNION ALL SELECT z.zone_id, z.created_at, z.name, z.parent_zone_id, z.zone_code "
			+ "FROM Zones z, zones_hierarchy WHERE z.parent_zone_id = zones_hierarchy.zone_id ) "
			+ "SELECT * FROM zones_hierarchy ")
	public List<Zones> getZonesInHierarchy();
	
//	@Override
//	default List<Zones> findAllZones(Sort) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//    List<Zones> findAllParentZone();

//    @Query("SELECT * FROM Zones WHERE zone_id = :zone_id")
//    public List<Zones> fi(@Param("zone_id") String zone_id);
}
