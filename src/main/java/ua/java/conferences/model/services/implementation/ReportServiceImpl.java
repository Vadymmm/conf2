package ua.java.conferences.model.services.implementation;

import lombok.RequiredArgsConstructor;
import ua.java.conferences.model.dao.ReportDAO;
import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.model.entities.Report;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.ReportService;

import java.util.*;

import static ua.java.conferences.exceptions.constants.Message.ENTER_CORRECT_TOPIC;
import static ua.java.conferences.utils.ConvertorUtil.*;
import static ua.java.conferences.utils.ValidatorUtil.*;

/**
 * Implementation of ReportService interface.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    /** Contains reportDAO field to work with ReportDAO */
    private final ReportDAO reportDAO;

    /**
     * Gets ReportDTO from action and calls DAO to add relevant entity. Validate report's topic.
     * Encode password for database. Converts UserDTO to User
     * @param reportDTO - DTO to be added as Report to database
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException with specific message
     */
    @Override
    public void addReport(ReportDTO reportDTO) throws ServiceException {
        validateComplexName(reportDTO.getTopic(), ENTER_CORRECT_TOPIC);
        Report report = convertDTOToReport(reportDTO);
        try {
            reportDAO.add(report);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Obtains instance of Report from DAO by id. Checks if id valid. Converts Report to ReportDTO
     * @param reportIdString - id as a String
     * @return UserDTO ReportDTO
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchReportException
     */
    @Override
    public ReportDTO getById(String reportIdString) throws ServiceException {
        ReportDTO reportDTO;
        long reportId = getReportId(reportIdString);
        try {
            Report report = reportDAO.getById(reportId).orElseThrow(NoSuchReportException::new);
            reportDTO = convertReportToDTO(report);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return reportDTO;
    }

    /**
     * Obtains list of all instances of Report from DAO. Convert Reports to ReportDTOs
     * @return List of ReportDTOs
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<ReportDTO> getAll() throws ServiceException {
        List<ReportDTO> reportDTOS = new ArrayList<>();
        try {
            List<Report> reports = reportDAO.getAll();
            reports.forEach(report -> reportDTOS.add(convertReportToDTO(report)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return reportDTOS;
    }

    /**
     * Calls DAO to get Event's Reports. Validate event ID. Convert Reports to ReportDTOs
     * @param eventIdString - id as a String
     * @return List of ReportDTO for this event
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public List<ReportDTO> viewEventsReports(String eventIdString) throws ServiceException {
        List<ReportDTO> reportDTOS = new ArrayList<>();
        try {
            long eventId = getLong(eventIdString);
            List<Report> reports = reportDAO.getEventsReports(eventId);
            reports.forEach(report -> reportDTOS.add(convertReportToDTO(report)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return reportDTOS;
    }

    /**
     * Calls DAO to get Speaker's Reports. Convert Reports to ReportDTOs
     * @param speakerId - Speaker's id
     * @return List of ReportDTO for this event
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public List<ReportDTO> viewSpeakersReports(long speakerId) throws ServiceException {
        List<ReportDTO> reportDTOS = new ArrayList<>();
        try {
            List<Report> reports = reportDAO.getSpeakersReports(speakerId);
            reports.forEach(report -> reportDTOS.add(convertReportToDTO(report)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return reportDTOS;
    }

    /**
     * Updates Report's topic. Validate ReportDTO. Converts ReportDTO to Report
     * @param dto - ReportDTO that contains Report's id and topic.
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException or
     * DuplicateEmailException
     */
    @Override
    public void update(ReportDTO dto) throws ServiceException {
        validateComplexName(dto.getTopic(), ENTER_CORRECT_TOPIC);
        Report report = convertDTOToReport(dto);
        try {
            reportDAO.update(report);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Calls DAO to set Speaker to Report
     * @param reportId - report id
     * @param speakerId - speaker id
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public boolean setSpeaker(long reportId, long speakerId) throws ServiceException {
        try {
            return reportDAO.setSpeaker(reportId, speakerId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Calls DAO to delete Speaker from Report
     * @param reportId - report id
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public void deleteSpeaker(long reportId) throws ServiceException {
        try {
            reportDAO.deleteSpeaker(reportId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Deletes Report entity from database. Validate id.
     * @param reportIdString - id as a String
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public void delete(String reportIdString) throws ServiceException {
        long reportId = getReportId(reportIdString);
        try {
            reportDAO.delete(reportId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}