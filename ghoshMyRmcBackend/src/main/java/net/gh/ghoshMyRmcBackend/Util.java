package net.gh.ghoshMyRmcBackend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {

	// ---------- answer Options --------------
	public static final String ANS_YES = "Yes";
	public static final String ANS_NO = "No";
	public static final String ANS_NA = "NA";

	// ---------conditions ---------------
	public static final String YES = "Yes";
	public static final String NO = "No";

	// ----------- return all conditions ----------
	public static List<String> getAllConditions() {
		List<String> conditions = new ArrayList<String>();
		conditions.add(YES);
		conditions.add(NO);
		return conditions;
	}

	// --------- states ------------
	public static final String ACTIVE_STATE = "Active";
	public static final String IN_ACTIVE_STATE = "Inactive";

	// ----------- return all states ----------
	public static List<String> getAllStates() {
		List<String> states = new ArrayList<String>();
		states.add(ACTIVE_STATE);
		states.add(IN_ACTIVE_STATE);
		return states;
	}

	// --------- Roles --------------
	public static final String SUPERADMIN = "Super Admin";
	public static final String ADMIN = "Admin";
	public static final String RADMIN = "R Admin";
	public static final String APPROVER = "Approver";
	public static final String ASSESSOR = "Assessor";
	public static final String SME = "SME";
	public static final String REVIEWER = "Reviewer";

	// ----------- return all states ----------
	public static List<String> getAllRoles() {
		List<String> roles = new ArrayList<String>();
		roles.add(ADMIN);
		roles.add(RADMIN);
		roles.add(APPROVER);
		roles.add(ASSESSOR);
		roles.add(SME);
		roles.add(REVIEWER);
		return roles;
	}

	// ---------- Phases ---------------
	public static final String STEADY_STATE_PHASE = "Steady-State";
	public static final String PRE_SIGNING_PHASE = "Pre-Signing";
	public static final String SUNSET_PHASE = "Sunset";

	// ----- return all phases ----------------
	public static List<String> getAllPhases() {
		List<String> phases = new ArrayList<String>();
		phases.add(STEADY_STATE_PHASE);
		phases.add(PRE_SIGNING_PHASE);
		phases.add(SUNSET_PHASE);
		return phases;
	}

	// ----------- Control risks ----------
	public static final String INTERNAL_RISK = "Internal";
	public static final String EXTERNAL_RISK = "External";
	public static final String BOTH = "Both";

	// ----- return all Risks ----------------
	public static List<String> getAllRisks() {
		List<String> risks = new ArrayList<String>();
		risks.add(INTERNAL_RISK);
		risks.add(EXTERNAL_RISK);
		risks.add(BOTH);
		return risks;
	}

	// ------------ control screens -----------
	public static final String SCREEN1 = "Screen1";
	public static final String SCREEN2 = "Screen2";
	public static final String NA = "NA";

	// ----- return all Screens ----------------
	public static List<String> getAllScreens() {
		List<String> screens = new ArrayList<String>();
		screens.add(SCREEN1);
		screens.add(SCREEN2);
		screens.add(NA);
		return screens;
	}

	// ------------ ratings -------------------
	public static final String RATING_A = "A";
	public static final String RATING_B = "B";
	public static final String RATING_C = "C";
	public static final String RATING_D = "D";
	public static final String RATING_NA = "NA";

	// ------------- return all ratings -------------
	public static List<String> getAllRatings() {
		List<String> ratings = new ArrayList<String>();
		ratings.add(RATING_A);
		ratings.add(RATING_B);
		ratings.add(RATING_C);
		ratings.add(RATING_D);
		ratings.add(RATING_NA);
		return ratings;
	}

	// --------- Account state --------------
	public static final String NOT_ASSIGNED_ACCOUNT = "Not Assigned";
	public static final String OPEN_ACCOUNT = "Open";
	public static final String ACTIVATED_ACCOUNT = "Active";

	// --------------Assessment Status ------------------
	public static final String INCOMPLETE_ASSESSMENT = "Incomplete";
	public static final String COMPLETE_ASSESSMENT = "Complete";
	public static final String SUBMITTED_ASSESSMENT = "Submitted";

	// --------------- Assessment Categories status --------------------
	public static final String INCOMPLETE_CATEGORY = "I";
	public static final String COMPLETE_CATEGORY = "C";
	public static final String SUBMITTED_CATEGORY = "S";

	// ----------------- confirmance status ------------------------
	public static final String REVIEW_PENDING = "Review Pending";
	public static final String REVIEW_COMPLETE = "Review Complete";
	public static final String CHANGE_REQUIRED = "Change Required";

	// -------- generating the random password -----------

	public static String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

}
