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

import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.repository.ActivityRepository;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import br.com.ufu.ppgeb.eeg.view.ActivityList;


@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Activity save( Activity activity ) {

        Assert.notNull( activity, "activity cannot be null." );

        validateActivity( activity );

        activity.setCreatedAt( new Date() );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth != null ) {
            activity.setCreatedBy( auth.getName() );
        }

        activity.setUpdatedAt( null );
        activity.setUpdatedBy( null );

        Activity activitySaved = activityRepository.save( activity );

        return activitySaved;
    }


    private void validateActivity( Activity activity ) {

        Assert.notNull( activity, "Activity cannot be null." );
        Assert.notNull( activity.getStartTime(), "start time cannot be nulll." );
        Assert.notNull( activity.getDuration(), "duration cannot be nulll." );
        Assert.hasText( activity.getDescription(), "description cannot be empty." );
    }


    @Override
    public List< Activity > findAll() {

        return activityRepository.findAll();
    }


    @Override
    public Activity findById( Long id ) {

        return activityRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Activity > findByFilter( Long examId ) {

        Assert.notNull( examId, "examId cannot be null." );

        List< Activity > list = activityRepository.findByExamId( examId );
        return list;
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        activityRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public List< Activity > updateList( ActivityList activityList ) {

        Assert.notNull( activityList, "ActivityList cannot be null." );
        Assert.notNull( activityList.getExamId(), "ExamId cannot be null." );

        List< Activity > oldActivities = activityRepository.findByExamId( activityList.getExamId() );

        List< Activity > activityUpdateList = new ArrayList<>();
        List< Activity > currentActivities = activityList.getActivities();

        if ( !CollectionUtils.isEmpty( currentActivities ) ) {

            for ( int i = 0; i < currentActivities.size(); i++ ) {

                if ( currentActivities.get( i ).getExamId() != null && !currentActivities.get( i ).getExamId().equals( activityList.getExamId() ) ) {
                    throw new IllegalArgumentException( currentActivities.get( i ) + " is not same examId in update=" + activityList.getExamId() );
                }

                if ( currentActivities.get( i ).getId() != null ) {
                    Boolean existActivity = false;

                    if ( !CollectionUtils.isEmpty( oldActivities ) ) {
                        for ( Activity oldActivity : oldActivities ) {
                            if ( currentActivities.get( i ).getId().equals( oldActivity.getId() ) ) {
                                existActivity = true;

                                if ( !currentActivities.get( i ).equals( oldActivity ) ) {
                                    currentActivities.get( i ).setCreatedAt( oldActivity.getCreatedAt() );
                                    currentActivities.get( i ).setCreatedBy( oldActivity.getCreatedBy() );
                                    currentActivities.get( i ).setUpdatedAt( new Date() );
                                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                                    if ( auth != null ) {
                                        currentActivities.get( i ).setUpdatedBy( auth.getName() );
                                    }
                                    validateActivity( currentActivities.get( i ) );
                                    currentActivities.set( i, activityRepository.save( currentActivities.get( i ) ) );
                                }
                                activityUpdateList.add( currentActivities.get( i ) );
                                oldActivities.remove( oldActivity );
                                break;
                            }
                        }
                    }

                    if ( existActivity == false ) {
                        throw new IllegalArgumentException(
                            "Activity with id=" + currentActivities.get( i ).getId() + " not exist by examID=" + activityList.getExamId() );
                    }
                }
            }

            currentActivities.removeAll( activityUpdateList );
            if ( !CollectionUtils.isEmpty( currentActivities ) ) {
                for ( Activity newActivity : currentActivities ) {
                    newActivity.setExamId( activityList.getExamId() );
                    newActivity.setCreatedAt( new Date() );
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if ( auth != null ) {
                        newActivity.setCreatedBy( auth.getName() );
                    }
                    save( newActivity );
                }
            }
        }

        for ( Activity oldActivity : oldActivities ) {
            activityRepository.delete( oldActivity );
        }

        currentActivities.addAll( activityUpdateList );

        return currentActivities;
    }
}
