package barqsoft.footballscores;

import android.content.Context;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEAGUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;

    public static int getLeagueStringResource(int league_num) {
        switch (league_num) {
            case SERIE_A:
                return R.string.league_serie_a;
            case PREMIER_LEAGUE:
                return R.string.league_premier_league;
            case CHAMPIONS_LEAGUE:
                return R.string.league_champions_league;
            case PRIMERA_DIVISION:
                return R.string.league_primera_division;
            case BUNDESLIGA:
                return R.string.league_bundesliga;
            default:
                return R.string.league_unknown;
        }
    }

    public static String getMatchDayString(Context context, int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return context.getString(R.string.format_group_stages, match_day);
            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.first_knockout_round);
            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.quarter_final);
            } else if (match_day == 11 || match_day == 12) {
                return context.getString(R.string.semi_final);
            } else {
                return context.getString(R.string.final_text);
            }
        } else {
            return context.getString(R.string.format_match_day, match_day);
        }
    }

    public static String getScoresString(Context context, int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return context.getString(R.string.format_scores, "", "");
        } else {
            return context.getString(R.string.format_scores, home_goals, awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(Context context, String teamName) {
        if (teamName == null) {
            return R.drawable.no_icon;
        }
        if (teamName.equals(context.getString(R.string.team_arsenal))) {
            return R.drawable.arsenal;
        } else if (teamName.equals(context.getString(R.string.team_manchester_united))) {
            return R.drawable.manchester_united;
        } else if (teamName.equals(context.getString(R.string.team_swansea_city))) {
            return R.drawable.swansea_city_afc;
        } else if (teamName.equals(context.getString(R.string.team_leicester_city))) {
            return R.drawable.leicester_city_fc_hd_logo;
        } else if (teamName.equals(context.getString(R.string.team_everton))) {
            return R.drawable.everton_fc_logo1;
        } else if (teamName.equals(context.getString(R.string.team_west_ham))) {
            return R.drawable.west_ham;
        } else if (teamName.equals(context.getString(R.string.team_tottenham_hotspur))) {
            return R.drawable.tottenham_hotspur;
        } else if (teamName.equals(context.getString(R.string.team_west_bromwich_albion))) {
            return R.drawable.west_bromwich_albion_hd_logo;
        } else if (teamName.equals(context.getString(R.string.team_sunderland))) {
            return R.drawable.sunderland;
        } else if (teamName.equals(context.getString(R.string.team_stoke_city))) {
            return R.drawable.stoke_city;
        } else {
            return R.drawable.no_icon;
        }
    }
}
