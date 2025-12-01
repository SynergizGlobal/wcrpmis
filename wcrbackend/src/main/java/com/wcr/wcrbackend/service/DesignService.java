package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.DTO.RevisionCheckDTO;
import com.wcr.wcrbackend.repo.IDesignRepo;

import jakarta.transaction.Transactional;
@Service
public class DesignService implements IDesignService {
	@Autowired
	private IDesignRepo designRepo;
	@Override
	public List<Design> getP6ActivitiesData(Design obj) throws Exception{
		// TODO Auto-generated method stub
		return designRepo.getP6ActivitiesData(obj);
	}
	@Override
	public List<Design> getDesignUpdateStructures(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignUpdateStructures(obj);
	}
	@Override
	public List<Design> getHodListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getHodListFilter(obj);
	}
	@Override
	public List<Design> getDepartmentListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDepartmentListFilter(obj);
	}
	@Override
	public List<Design> getContractListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getContractListFilter(obj);
	}
	@Override
	public List<Design> getStructureListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureListFilter(obj);
	}
	@Override
	public List<Design> getStructureIdsListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureIdsListFilter(obj);
	}
	@Override
	public List<Design> getDrawingTypeListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDrawingTypeListFilter(obj);
	}
	@Override
	public List<Design> getDesigns(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesigns(obj);
	}
	@Override
	public List<Design> getDesignsList(Design obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignsList(obj, startIndex, offset, searchParameter);
	}
	@Override
	public int getTotalRecords(Design obj, String searchParameter) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getTotalRecords(obj, searchParameter);
	}
	@Override
	public List<Design> getDrawingRepositoryDesignsList(Design obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDrawingRepositoryDesignsList(obj, startIndex, offset, searchParameter);
	}
	@Override
	public int getTotalDrawingRepositoryRecords(Design obj, String searchParameter) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getTotalDrawingRepositoryRecords(obj, searchParameter);
	}
	@Override
	public List<Design> getProjectsListForDesignForm(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getProjectsListForDesignForm(obj);
	}
	@Override
	public List<Design> getContractList() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getContractList();
	}
	@Override
	public List<Design> getPreparedByList() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getPreparedByList();
	}
	@Override
	public List<Design> componentList() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.componentList();
	}
	@Override
	public List<Design> getApprovingRailwayList() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getApprovingRailwayList();
	}
	@Override
	public List<Design> structureList() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.structureList();
	}
	@Override
	public List<Design> drawingTypeList() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.drawingTypeList();
	}
	@Override
	public List<Design> getRevisionStatuses() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getRevisionStatuses();
	}
	@Override
	public List<Design> getApprovalAuthority() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getApprovalAuthority();
	}
	@Override
	public List<Design> getStage() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStage();
	}
	@Override
	public List<Design> getSubmitted() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getSubmitted();
	}
	@Override
	public List<Design> getStructureId() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureId();
	}
	@Override
	public List<Design> getSubmssionpurpose() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getSubmssionpurpose();
	}
	@Override
	public List<Design> getDesignFileType() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignFileType();
	}
	@Override
	public List<Design> getAsBuiltStatuses() throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getAsBuiltStatuses();
	}
	@Override
	public Design getDesignDetails(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignDetails(obj);
	}
	@Override
	public List<Design> getStructureTypeListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureTypeListFilter(obj);
	}
	@Override
	public List<Design> getContractsListForDesignForm(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getContractsListForDesignForm(obj);
	}
	@Override
	public List<Design> getComponentsforDesign(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getComponentsforDesign(obj);
	}
	@Override
	public List<Design> getStructureIdsforDesign(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureIdsforDesign(obj);
	}
	@Override
	public List<Design> getStructureTypesforDesign(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureTypesforDesign(obj);
	}
	@Override
	public List<Design> getDesignUploadsList(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignUploadsList(obj);
	}
	@Override
	public List<Design> getHodList(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getHodList(obj);
	}
	@Override
	public List<Design> getDyHodList(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDyHodList(obj);
	}
	@Override
	@Transactional
	public String addDesign(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.addDesign(obj);
	}
	@Override
	@Transactional
	public String updateDesign(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.updateDesign(obj);
	}
	@Override
	public List<Design> getDesignRevisions(Design design) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignRevisions(design);
	}
	@Override
	@Transactional
	public boolean saveDesignDataUploadFile(Design design) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.saveDesignDataUploadFile(design);
	}
	@Override
	@Transactional
	public int[] uploadDesignsNew(List<Design> designsList, List<Design> designsRevListupdate, List<Design> designsRevInsert ) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.uploadDesignsNew(designsList,designsRevListupdate, designsRevInsert);
	}
	@Override
	public boolean updateDesignStatusBulk(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.updateDesignStatusBulk(obj);
	}
	@Override
	public RevisionCheckDTO revisionCheckUpdateOrInsert(String designSeqId, String revision) throws Exception {

		RevisionCheckDTO  data= designRepo.getRevisionCheckByDesignIdFkAndRevision(designSeqId, revision);

	    return  data;
	}


}
