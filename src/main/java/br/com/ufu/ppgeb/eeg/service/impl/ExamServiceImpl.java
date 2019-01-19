package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.repository.ExamEquipmentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamMedicamentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
import br.com.ufu.ppgeb.eeg.service.ExamService;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;


@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamMedicamentRepository examMedicamentRepository;

    @Autowired
    private ExamEquipmentRepository examEquipmentRepository;

    @Autowired
    private MedicamentService medicamentService;

    @Autowired
    private EquipmentService equipmentService;


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Exam save( Exam exam ) {

        Assert.notNull( exam, "exam cannot be null." );

        validateExam( exam );

        exam.setCreatedAt( new Date() );
        // TODO: pegar o usuario logado
        exam.setCreatedBy( "system" );

        exam.setUpdatedAt( null );
        exam.setUpdatedBy( null );

        Exam examSaved = examRepository.save( exam );

        return examSaved;
    }


    private void validateExam( Exam exam ) {

        Assert.notNull( exam, "Exam cannot be null." );
        // Assert.notNull( exam.getMedicalRecord(), "medicalRecord cannot be empty." );
        // Assert.notNull( exam.getMedicalRequest(), "medicalRequest cannot be empty."
        // );
        // Assert.hasText( exam.getSector(), "sector cannot be empty." );
        // Assert.hasText( exam.getDoctorRequestant(), "doctorRequestant cannot be
        // empty." );
        // Assert.hasText( exam.getUser(), "user cannot be empty." );
        // Assert.notNull( exam.getRequestDate(), "requestDate cannot be empty." );
    }


    @Override
    public List< Exam > findAll() {

        return examRepository.findAll();
    }


    @Override
    public Exam findById( Long id ) {

        return examRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Exam > findByFilter( Long medicalRecord, Long medicalRequest, Long patientId, String doctorRequestant ) {

        if ( StringUtils.isBlank( doctorRequestant ) && medicalRequest == null && patientId == null && medicalRecord == null ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        List< Exam > list = null;

        list = examRepository.findByFilter( medicalRecord, medicalRequest, patientId, doctorRequestant );
        return list;
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        examRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Exam update( Exam exam ) {

        Assert.notNull( exam, "exam cannot be null." );

        validateExam( exam );
        Assert.notNull( exam.getId(), "exam ID cannot be null." );

        Exam oldExam = examRepository.getOne( exam.getId() );

        if ( oldExam == null ) {
            throw new IllegalArgumentException( "Not exist exam with this Id=" + exam.getId() );
        }

        if ( !oldExam.equals( exam ) ) {
            // oldExam.setMedicalRecord( exam.getMedicalRecord() );
            // oldExam.setMedicalRequest( exam.getMedicalRequest() );

            if ( !exam.getPatient().getId().equals( oldExam.getPatient().getId() ) ) {
                throw new IllegalArgumentException( "Patient ID is different. Neew=" + exam.getPatient().getId() + ", Old=" + oldExam.getPatient().getId() );
            }

            oldExam.setAchievementDate( exam.getAchievementDate() );
            // oldExam.setAgreement( exam.getAgreement() );
            // oldExam.setCityOrigin( exam.getCityOrigin() );
            // oldExam.setClinicOrigin( exam.getClinicOrigin() );
            // oldExam.setDoctorRequestant( exam.getDoctorRequestant() );
            // oldExam.setRequestDate( exam.getRequestDate() );
            // oldExam.setSector( exam.getSector() );
            // oldExam.setUser( exam.getUser() );

            oldExam.setUpdatedAt( new Date() );
            // TODO: pegar o usuario logado
            oldExam.setUpdatedBy( "system" );

            exam = examRepository.save( oldExam );
        }

        return exam;

    }


    private void registerUnregisteredMedicaments( List< ExamMedicament > examMedicamentList ) {

        if ( examMedicamentList != null && examMedicamentList.size() > 0 ) {
            for ( ExamMedicament examMedicament : examMedicamentList ) {
                Medicament medicament = examMedicament.getMedicament();
                Assert.notNull( medicament, "medicament cannot be null." );
                Assert.hasText( medicament.getName(), "medicament name cannot be empty." );

                if ( medicament.getId() == null ) {
                    examMedicament.setMedicament( medicamentService.save( medicament ) );
                }
            }
        }
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Exam updateExamMedicament( Exam exam ) {

        Assert.notNull( exam, "exam cannot be null." );
        Assert.notNull( exam.getId(), "exam ID cannot be null." );

        registerUnregisteredMedicaments( exam.getExamMedicaments() );
        validateExamMedicamentList( exam.getExamMedicaments() );

        Exam oldExam = examRepository.getOne( exam.getId() );

        if ( oldExam == null ) {
            throw new IllegalArgumentException( "Not exist exam with this Id=" + exam.getId() );
        }

        List< ExamMedicament > oldExamMedicamentList = oldExam.getExamMedicaments();

        List< ExamMedicament > examMedicamentUpdateList = new ArrayList<>();

        if ( exam.getExamMedicaments() != null && exam.getExamMedicaments().size() > 0 ) {

            List< ExamMedicament > examMedicamentList = exam.getExamMedicaments();
            for ( int i = 0; i < examMedicamentList.size(); i++ ) {

                if ( examMedicamentList.get( i ).getId() != null ) {
                    Boolean existExamMedicament = false;

                    if ( oldExamMedicamentList != null && oldExamMedicamentList.size() > 0 ) {
                        for ( ExamMedicament oldContact : oldExamMedicamentList ) {
                            if ( examMedicamentList.get( i ).getId().equals( oldContact.getId() ) ) {
                                existExamMedicament = true;

                                examMedicamentList.get( i ).setUpdatedAt( new Date() );
                                examMedicamentList.get( i ).setUpdatedBy( "SYSTEM" );
                                examMedicamentList.set( i, examMedicamentRepository.save( examMedicamentList.get( i ) ) );
                                examMedicamentUpdateList.add( examMedicamentList.get( i ) );
                                break;
                            }
                        }
                    }

                    if ( existExamMedicament == false ) {
                        throw new IllegalArgumentException(
                            "Exam Medicament with id=" + examMedicamentList.get( i ).getId() + " not exist by examID=" + exam.getId() );
                    }
                }
            }
        }

        exam.getExamMedicaments().removeAll( examMedicamentUpdateList );
        if ( exam.getExamMedicaments() != null && exam.getExamMedicaments().size() > 0 ) {
            for ( ExamMedicament newExamMedicament : exam.getExamMedicaments() ) {
                newExamMedicament.setCreatedAt( new Date() );
                newExamMedicament.setCreatedBy( "SYSTEM" );
                examMedicamentRepository.save( newExamMedicament );
            }
        }

        oldExamMedicamentList.removeAll( examMedicamentUpdateList );
        for ( ExamMedicament oldExamMedicament : oldExamMedicamentList ) {
            examMedicamentRepository.delete( oldExamMedicament );
        }

        exam.getExamMedicaments().addAll( examMedicamentUpdateList );

        return exam;
    }


    private void validateExamMedicamentList( List< ExamMedicament > examMedicamentList ) {

        if ( examMedicamentList != null && examMedicamentList.size() > 0 ) {
            for ( ExamMedicament examMedicament : examMedicamentList ) {
                validateExamMedicament( examMedicament );
            }
        }
    }


    private void validateExamMedicament( ExamMedicament examMedicament ) {

        Assert.notNull( examMedicament, "examMedicament cannot be null" );
        Assert.notNull( examMedicament.getAmount(), "examMedicament-amount cannot be null" );
        Assert.notNull( examMedicament.getMedicament(), "examMedicament-medicament cannot be null" );
        Assert.notNull( examMedicament.getMedicament().getId(), "examMedicament-medicament-id cannot be null" );
        Assert.notNull( examMedicament.getUnit(), "examMedicament-unit cannot be null" );
        Assert.notNull( examMedicament.getUnit().getId(), "examMedicament-unit-id cannot be null" );
    }


    private void registerUnregisteredEquipments( List< ExamEquipment > examEquipmentList ) {

        if ( examEquipmentList != null && examEquipmentList.size() > 0 ) {
            for ( ExamEquipment examEquipment : examEquipmentList ) {
                Equipment equipment = examEquipment.getEquipment();
                Assert.notNull( equipment, "equipment cannot be null." );
                Assert.hasText( equipment.getName(), "equipment name cannot be empty." );

                if ( equipment.getId() == null ) {
                    examEquipment.setEquipment( equipmentService.save( equipment ) );
                }
            }
        }
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Exam updateExamEquipment( Exam exam ) {

        Assert.notNull( exam, "exam cannot be null." );
        Assert.notNull( exam.getId(), "exam ID cannot be null." );

        registerUnregisteredEquipments( exam.getExamEquipments() );
        validateExamEquipmentList( exam.getExamEquipments() );

        Exam oldExam = examRepository.getOne( exam.getId() );

        if ( oldExam == null ) {
            throw new IllegalArgumentException( "Not exist exam with this Id=" + exam.getId() );
        }

        List< ExamEquipment > oldExamEquipmentList = oldExam.getExamEquipments();

        List< ExamEquipment > examEquipmentUpdateList = new ArrayList<>();

        if ( exam.getExamEquipments() != null && exam.getExamEquipments().size() > 0 ) {

            List< ExamEquipment > examEquipmentList = exam.getExamEquipments();
            for ( int i = 0; i < examEquipmentList.size(); i++ ) {

                if ( examEquipmentList.get( i ).getId() != null ) {
                    Boolean existExamEquipment = false;

                    if ( oldExamEquipmentList != null && oldExamEquipmentList.size() > 0 ) {
                        for ( ExamEquipment oldContact : oldExamEquipmentList ) {
                            if ( examEquipmentList.get( i ).getId().equals( oldContact.getId() ) ) {
                                existExamEquipment = true;

                                examEquipmentList.get( i ).setUpdatedAt( new Date() );
                                examEquipmentList.get( i ).setUpdatedBy( "SYSTEM" );
                                examEquipmentList.set( i, examEquipmentRepository.save( examEquipmentList.get( i ) ) );
                                examEquipmentUpdateList.add( examEquipmentList.get( i ) );
                                break;
                            }
                        }
                    }

                    if ( existExamEquipment == false ) {
                        throw new IllegalArgumentException(
                            "Exam Equipment with id=" + examEquipmentList.get( i ).getId() + " not exist by examID=" + exam.getId() );
                    }
                }
            }
        }

        exam.getExamEquipments().removeAll( examEquipmentUpdateList );
        if ( exam.getExamEquipments() != null && exam.getExamEquipments().size() > 0 ) {
            for ( ExamEquipment newExamEquipment : exam.getExamEquipments() ) {
                newExamEquipment.setCreatedAt( new Date() );
                newExamEquipment.setCreatedBy( "SYSTEM" );
                examEquipmentRepository.save( newExamEquipment );
            }
        }

        oldExamEquipmentList.removeAll( examEquipmentUpdateList );
        for ( ExamEquipment oldExamEquipment : oldExamEquipmentList ) {
            examEquipmentRepository.delete( oldExamEquipment );
        }

        exam.getExamEquipments().addAll( examEquipmentUpdateList );

        return exam;
    }


    private void validateExamEquipmentList( List< ExamEquipment > examEquipmentList ) {

        if ( examEquipmentList != null && examEquipmentList.size() > 0 ) {
            for ( ExamEquipment examEquipment : examEquipmentList ) {
                validateExamEquipment( examEquipment );
            }
        }
    }


    private void validateExamEquipment( ExamEquipment examEquipment ) {

        Assert.notNull( examEquipment, "examEquipment cannot be null" );
        Assert.notNull( examEquipment.getAmount(), "examEquipment-amount cannot be null" );
        Assert.notNull( examEquipment.getEquipment(), "examEquipment-equipment cannot be null" );
        Assert.notNull( examEquipment.getEquipment().getId(), "examEquipment-equipment-id cannot be null" );
        Assert.notNull( examEquipment.getUnit(), "examEquipment-unit cannot be null" );
        Assert.notNull( examEquipment.getUnit().getId(), "examEquipment-unit-id cannot be null" );
    }
}
