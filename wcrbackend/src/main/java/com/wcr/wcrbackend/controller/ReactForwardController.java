package com.wcr.wcrbackend.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class ReactForwardController {

    @RequestMapping(value = {

        /* ===================== Common & Dashboards ===================== */
        "/",
        "/home",
        "/admin",
        "/work-overview-dashboard",
        "/project-overview-dashboard",
        "/execution-overview-dashboard",
        "/contract-overview-dashboard",
        "/progress-table",
        "/timeline-schedule-dashboard",
        "/issues-overview-dashboard",
        "/monthwise-progress-dashboard",
        "/monthwise-plan-dashboard",
        "/daily-progress-dashboard",
        "/gis-map-dashboard",
        "/site-photos-dashboard",
        "/Works",

        /* ===================== Reference Forms : Contract ===================== */
        "/bank-guarantee-type",
        "/contractFileType",
        "/contractorSpecialization",
        "/contractType",

        /* ===================== Reference Forms : Design ===================== */
        "/asBuiltStatus",
        "/as-built-status",
        "/drawingType",
        "/approvalAuthority",
        "/stage",
        "/submittedBy",
        "/designFileType",
        "/purposeOfSubmission",
        "/designExecutives",

        /* ===================== Reference Forms : Execution & Monitoring ===================== */
        "/structure-type",

        /* ===================== Reference Forms : Issues ===================== */
        "/issueCategory",
        "/issueCategoryTitle",
        "/issueContractCategory",
        "/issueFileType",
        "/issuePriority",
        "/issueOtherOrganisation",
        "/issueStatus",

        /* ===================== Reference Forms : Land Acquisition ===================== */
        "/laCategory",
        "/laSubCategory",
        "/laStatus",
        "/laFileType",
        "/laLandStatus",
        "/laExecutives",

        /* ===================== Reference Forms : Others ===================== */
        "/dashboard-type",
        "/user-type",
        "/general-status",
        "/execution-status",

        /* ===================== Reference Forms : Utility Shifting ===================== */
        "/utility-category",
        "/utility-execution-agency",
        "/utility-requirement-stage",
        "/utility-status",
        "/utility-types",
        "/utility-shifting-filetyp",
        "/utility-shifting-executives",

        /* ===================== Update Forms ===================== */
        "/updateforms/project",
        "/updateforms/project/projectform",

        "/updateforms/structure",
        "/updateforms/structure/addstructureform",
        "/updateforms/structure-form",

        "/updateforms/contract",
        "/updateforms/contract/add-contract-form",

        "/updateforms/contractor",
        "/updateforms/contractor/add-contractor-form",

        "/updateforms/p6-new-data",
        "/updateforms/new-activities-update",

        "/updateforms/design",
        "/updateforms/design/add-design-form",
        "/wcrpmis/design/export-design",

        "/updateforms/issues",
        "/updateforms/issues/issuesform",
        "/admin/reference-forms",

        "/updateforms/land-acquisition",
        "/updateforms/land-acquisition/landacquisitionform",

        "/updateforms/utilityshifting",
        "/updateforms/utilityshifting/add-utility-shifting",
        "/insuranceType" ,
        "/revisionStatus" ,
        "/bankName" ,
        "/contractExecutives" ,
        "/designStage" ,
        "/railway",
        "/documentType",
        "/department" ,
        "/projectType",
        "/utilityShiftingfiletyp",
        "/utilityExecutionagency",
        "/utilityTypes",
        "/utilityRequirementstage",
        "/utilityCategory",
        "/utilityStatus",
        "/utilityShiftingexecutives"       
    })
    public String forwardReact() {
        return "forward:/index.html";
    }
}





