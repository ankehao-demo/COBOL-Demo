/**
 * Sub-Program Calls — COBOL CALL/CANCEL demonstrations.
 *
 * Migrated from: sub_program/main_app.cbl, sub_program/sub.cbl
 *
 * Demonstrates COBOL sub-program calling conventions: BY CONTENT (pass copies,
 * caller values unchanged), BY REFERENCE (pass references, caller sees changes),
 * and CANCEL (reset sub-program working-storage). Also shows the difference
 * between WORKING-STORAGE (persists) and LOCAL-STORAGE (fresh each call).
 */
package com.coboldemo.subprogram;
