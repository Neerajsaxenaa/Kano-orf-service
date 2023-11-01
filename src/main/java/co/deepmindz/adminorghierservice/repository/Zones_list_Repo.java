package co.deepmindz.adminorghierservice.repository;

import java.util.List;
import java.util.Optional;

import co.deepmindz.adminorghierservice.dto.ParentZoneDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import co.deepmindz.adminorghierservice.models.Zones_list;

@Component
public interface Zones_list_Repo extends JpaRepository<Zones_list, String> {

    @Query(value = "select z from Zones_list z where z.belongs_to_zone IN :belongs_to_zone")
    public List<Zones_list> getZonesUsingLinkedZonesUsingInClause(@Param("belongs_to_zone") List<String> belongs_to_zone);

    @Query(value = "select linked_zone_list from Zones_list where belongs_to_zone = :belongs_to_zone")
    public List<String> getZonesUsingLinkedZones(@Param("belongs_to_zone") String belongs_to_zone);


    @Query(value = "select * from Zones_list where _id = :_id", nativeQuery = true)
    public List<Zones_list> getListParentZoneItem(@Param("_id") String _id);


    @Query(value = "SELECT new co.deepmindz.adminorghierservice.dto.ParentZoneDTO(z._id, z.name, z.linked_zone_list, z.code, z.belongs_to_zone) FROM Zones_list z WHERE z.linked_zone_list IS NULL OR z.linked_zone_list = ''")
    public List<ParentZoneDTO> getParentZoneItem();


    @Query(value = "select * from Zones_list where belongs_to_zone = :belongs_to_zone AND _id = :getParent_zone_list_id", nativeQuery = true)
    public List<Zones_list> getListParentZoneItems(@Param("belongs_to_zone") String belongs_to_zone, @Param("getParent_zone_list_id") String getParent_zone_list_id);


//
//	@Query(value = "select z from Zones_list z where z.belongs_to_zone IN :belongs_to_zone")
//	public Optional<Object> getZoneListIdByBelongsToId(@Param("belongs_to_zone") String belongs_to_zone);

    @Query(value = "select z from Zones_list z where z.linked_zone_list = :linked_zone")
    public List<Zones_list> getAllZonesByRelationshipId(String linked_zone);


    @Query(value = "select z from Zones_list z where z.linked_zone_list = :linked_zone")
    public List<Zones_list> getAllParentListData(String linked_zone);
}
