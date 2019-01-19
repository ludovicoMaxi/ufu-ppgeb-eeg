package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Exam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.repository.EpochRepository;
import br.com.ufu.ppgeb.eeg.service.EpochService;


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
        // TODO: pegar o usuario logado
        epoch.setCreatedBy( "SYSTEM" );

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
        exam.setId(examId);
        return list = epochRepository.findByExam( exam );
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        epochRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public List<Epoch> updateList( List<Epoch> epoch ) {


        return null;
    }
}
