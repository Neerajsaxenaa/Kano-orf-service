package co.deepmindz.adminorghierservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import co.deepmindz.adminorghierservice.models.SSUser;
import co.deepmindz.adminorghierservice.models.Zones_list;
import jakarta.transaction.Transactional;

@Component
public interface SSUserRepository extends JpaRepository<SSUser, String> {

	public SSUser findByUsername(@Param("username") String username);

	public List<SSUser> findByLinkedSupervisors(String[] roleId);

	public List<SSUser> findByLinkedZone(String zoneId);

	@Query(value = "select z from Zones_list z where z._id IN :zoneid")
	public List<Zones_list> getUserAllZonesDetails(@Param("zoneid") List<String> zoneid);

	@Query(value = "select * from ssuser where is_team_lead is false AND status ='ACTIVE' AND linked_zone =:zoneId", nativeQuery = true)
	public List<SSUser> getTeamMemberByZoneId(@Param("zoneId") String zoneId);

	@Query(value = "select s from SSUser s where s.user_id IN :memberIds")
	public List<SSUser> findByIds(@Param("memberIds") String[] memberIds);

	@Modifying
	@Transactional
	@Query(value = "update SSUser SET is_team_lead = TRUE WHERE user_id IN :userId", nativeQuery = true)
	public void appointAsTeamLead(@Param("userId") List<String> userId);

	@Query("select phoneNumber from SSUser where user_id = :ssuserid")
	public String getPhoneNumberOfSSUserId(String ssuserid);
}
