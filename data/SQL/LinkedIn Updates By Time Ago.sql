SELECT companyString,
p.lipageid as 'liPageId',
timeAgo,
count(*),
sum(likesCount) as 'likesCount',
sum(commentsCount) as 'commentsCount'
FROM socialcrawler.liupdate u
join lipage p on u.lipage_lipageid = p.lipageid
group by p.lipageid, timeAgo
