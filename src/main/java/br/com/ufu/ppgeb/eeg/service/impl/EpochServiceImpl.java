package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.repository.EpochRepository;
import br.com.ufu.ppgeb.eeg.service.EpochService;
import br.com.ufu.ppgeb.eeg.view.EpochList;


@Service
public class EpochServiceImpl implements EpochService {

    @Autowired
    private EpochRepository epochRepository;


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Epoch save( Epoch epoch ) {

        Assert.notNull( epoch, "epoch cannot be null." );

        validateEpoch( epoch );

        epoch.setCreatedAt( new Date() );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth != null ) {
            epoch.setCreatedBy( auth.getName() );
        }

        epoch.setUpdatedAt( null );
        epoch.setUpdatedBy( null );

        Epoch epochSaved = epochRepository.save( epoch );

        return epochSaved;
    }


    private void validateEpoch( Epoch epoch ) {

        Assert.notNull( epoch, "Epoch cannot be null." );
        Assert.notNull( epoch.getStartTime(), "start time cannot be nulll." );
        Assert.notNull( epoch.getDuration(), "duration cannot be nulll." );
        Assert.hasText( epoch.getDescription(), "description cannot be empty." );
    }


    @Override
    public List< Epoch > findAll() {

        return epochRepository.findAll();
    }


    @Override
    public Epoch findById( Long id ) {

        return epochRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Epoch > findByFilter( Long examId ) {

        List< Epoch > list = null;

        Assert.notNull( examId, "examId cannot be null." );

        Exam exam = new Exam();
        exam.setId( examId );
        return list = epochRepository.findByExam( exam );
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        epochRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public List< Epoch > updateList( EpochList epochList ) {

        Assert.notNull( epochList, "EpochList cannot be null." );
        Assert.notNull( epochList.getExamId(), "ExamId cannot be null." );

        List< Epoch > oldEpochs = epochRepository.findByExam( new Exam( epochList.getExamId() ) );

        List< Epoch > epochUpdateList = new ArrayList<>();
        List< Epoch > currentEpochs = epochList.getEpochs();

        if ( !CollectionUtils.isEmpty( currentEpochs ) ) {

            for ( int i = 0; i < currentEpochs.size(); i++ ) {

                if ( currentEpochs.get( i ).getExam() != null && currentEpochs.get( i ).getExam().getId() != epochList.getExamId() ) {
                    throw new IllegalArgumentException( currentEpochs.get( i ) + " is not same examId in update=" + epochList.getExamId() );
                }

                if ( currentEpochs.get( i ).getId() != null ) {
                    Boolean existEpoch = false;

                    if ( !CollectionUtils.isEmpty( oldEpochs ) ) {
                        for ( Epoch oldEpoch : oldEpochs ) {
                            if ( currentEpochs.get( i ).getId().equals( oldEpoch.getId() ) ) {
                                existEpoch = true;

                                currentEpochs.get( i ).setCreatedAt( oldEpoch.getCreatedAt() );
                                currentEpochs.get( i ).setCreatedBy( oldEpoch.getCreatedBy() );
                                currentEpochs.get( i ).setUpdatedAt( new Date() );
                                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                                if ( auth != null ) {
                                    currentEpochs.get( i ).setUpdatedBy( auth.getName() );
                                }
                                validateEpoch( currentEpochs.get( i ) );
                                currentEpochs.set( i, epochRepository.save( currentEpochs.get( i ) ) );
                                epochUpdateList.add( currentEpochs.get( i ) );
                                break;
                            }
                        }
                    }

                    if ( existEpoch == false ) {
                        throw new IllegalArgumentException(
                                "Epoch with id=" + currentEpochs.get( i ).getId() + " not exist by examID=" + epochList.getExamId() );
                    }
                }
            }

            currentEpochs.removeAll( epochUpdateList );
            if ( !CollectionUtils.isEmpty( currentEpochs ) ) {
                Exam examCurrent = new Exam();
                examCurrent.setId( epochList.getExamId() );
                for ( Epoch newEpoch : currentEpochs ) {
                    newEpoch.setExam( examCurrent );
                    newEpoch.setCreatedAt( new Date() );
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if ( auth != null ) {
                        newEpoch.setCreatedBy( auth.getName() );
                    }
                    save( newEpoch );
                }
            }
        }

        oldEpochs.removeAll( epochUpdateList );
        for ( Epoch oldEpoch : oldEpochs ) {
            epochRepository.delete( oldEpoch );
        }

        currentEpochs.addAll( epochUpdateList );

        return currentEpochs;
    }
}
