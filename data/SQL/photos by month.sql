SELECT companyString, date_format(createdTime, '%Y-%m') as 'Date', count(*), fbphotoid, sum(if(placeId is null, 0, 1)) as 'hasPlace',
	sum(if(createdTime = updatedTime, 1, 0)) as 'Updated Later'

FROM socialcrawler.fbphoto photo
join fbpage p on p.fbpageid = photo.fbpage_fbpageid
where !(hour(createdTime) = 0 and minute(createdTime) = 0 and second(createdTime) = 0)
AND !(hour(createdTime) = 1 and minute(createdTime) = 0 and second(createdTime) = 0)
AND !(hour(createdTime) = 13 and minute(createdTime) = 0 and second(createdTime) = 0)
and photo.fromId = p.fbid
group by p.fbpageid, date_format(createdTime, '%Y-%m')