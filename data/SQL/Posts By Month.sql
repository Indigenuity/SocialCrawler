SELECT companyString,
	fbId, 
    fbPageId, 
    date_format(realCreatedDate, '%Y-%m') as 'date', 
    count(*), 
    sum(shares) as 'shares', 
    sum(if(caption is null, 0, 1)) as 'hasCaption',
    sum(if(message is null, 1, 0)) as 'blankMessage',
	sum(if(realCreatedDate = realLastUpdated, 1, 0)) as 'updatedLater', 
    sum(likesCount) as likesCount, 
    sum(commentsCount) as commentsCount,
    sum(reactionsCount) as reactionsCount, 
    sum(if(parentId is null, 0, 1)) as 'hasParent',
    sum(if(place = 'null', 0, 1)) as 'hasPlace', 
    sum(if(messageTags = '[]', 0, 1)) as 'hasMessageTags', 
    sum(if(source is null, 0, 1)) as 'hasSource',
    sum(if(toId = '', 0, 1)) as 'hasTo',
    sum(if(app = 'null', 0, 1)) as 'postedFromApp',
    sum(if(statusType = 'added_photos', 1, 0)) as 'added_photosStory',
    sum(if(statusType = 'shared_story', 1, 0)) as 'shared_storyStory',
    sum(if(statusType = 'added_video', 1, 0)) as 'added_videoStory',
    sum(if(statusType = 'mobile_status_update', 1, 0)) as 'mobile_status_updateStory',
    sum(if(statusType = 'published_story', 1, 0)) as 'published_storyStory',
    sum(if(statusType = 'created_note', 1, 0)) as 'created_noteStory',
    sum(if(statusType = 'wall_post', 1, 0)) as 'wall_postStory',
    sum(if(statusType = 'created_event', 1, 0)) as 'created_eventStory',
    sum(if(statusType is null, 1, 0)) as 'noTypeStory'
FROM socialcrawler.fbpost post
join fbpage p on p.fbpageid = post.fbpage_fbpageid
where !(hour(realCreatedDate) = 0 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
AND !(hour(realCreatedDate) = 1 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
AND !(hour(realCreatedDate) = 13 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
and post.fromId = p.fbid
group by p.fbpageid, date_format(realCreatedDate, '%Y-%m')
order by hasTargeting desc