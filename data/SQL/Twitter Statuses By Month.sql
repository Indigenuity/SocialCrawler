SELECT companyString, 
twitteruserid, 
tu.id,
date_format(t.createdAt, '%Y-%m') as 'date',
count(*),
sum(if(extendedMediaEntities = '', 0, 1)) as 'hasExtendedMediaEntities',
sum(favoriteCount) as 'favoriteCount',
sum(if(hashtagEntities = '', 0, 1)) as 'hasHashtagEntities',
sum(if(inReplyToScreenName is null, 0, 1)) as 'isReply',
sum(if(latitude is null, 0, 1)) as 'hasCoords',
sum(if(mediaEntities = '', 0, 1)) as 'hasMediaEntities',
sum(if(mentionEntities = '', 0, 1)) as 'hasMentionEntities',
sum(if(latitude is null, 0, 1)) as 'hasPlace',
sum(if(possiblySensitive = 0, 0, 1)) as 'possiblySensitive',
sum(if(quotedStatusId = -1, 0, 1)) as 'isStatusQuote',
sum(retweetCount) as 'retweetCount',
sum(if(retweetedStatus is null, 0, 1)) as 'isRetweet',
sum(if(urlEntities = '', 0, 1)) as 'hasUrlEntities'
FROM socialcrawler.tweet t
join twitteruser tu on tu.twitteruserid = t.twitteruser_twitteruserid
group by tu.twitteruserid, date_format(t.createdAt, '%Y-%m')