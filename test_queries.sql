select m.match_id, m.start_time, m.radiant_team_id, m.dire_team_id, m.radiant_win, (SELECT name FROM teams WHERE team_id = m.radiant_team_id) as radiant_name, (SELECT name FROM teams WHERE team_id = m.dire_team_id) as dire_name FROM matches m where leagueid=4951;

select m.match_id, m.start_time, m.leagueid, m.radiant_team_id, m.dire_team_id, m.radiant_win, tm.radiant, tm.team_id, t.name, (SELECT name FROM teams WHERE team_id = m.radiant_team_id) as radiant_name, (SELECT name FROM teams WHERE team_id = m.dire_team_id) as dire_name FROM matches m, team_match tm, teams t where leagueid=4951 and m.match_id = tm.match_id and ((m.radiant_win = true and tm.radiant = true) OR (m.radiant_win = false and tm.radiant = false)) AND tm.team_id = t.team_id;

select m.match_id, m.leagueid, m.radiant_team_id, m.dire_team_id FROM matches m where leagueid IN (SELECT leagueid FROM leagues WHERE tier='professional');

SELECT COUNT(match_id), team_id FROM team_match GROUP BY team_id ORDER BY count DESC;

SELECT * FROM leagues WHERE tier='professional' AND leagueid NOT IN (0);

SELECT * FROM teams WHERE team_id NOT IN (0);

select m.match_id, m.start_time, m.leagueid, m.radiant_team_id, m.dire_team_id, m.radiant_win FROM matches m where leagueid=4951;
