update fbpage as fbp inner join (SELECT fbpageid, min(post.realcreateddate) as themin FROM socialcrawler.fbpost post
join fbpage p on p.fbpageid = post.fbpage_fbpageid
where !(hour(realCreatedDate) = 0 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
AND !(hour(realCreatedDate) = 1 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
AND !(hour(realCreatedDate) = 12 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
AND !(hour(realCreatedDate) = 14 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
AND !(hour(realCreatedDate) = 13 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0)
group by p.fbpageid) monster
on fbp.fbpageid = monster.fbpageid
set fbp.firstpost = themin