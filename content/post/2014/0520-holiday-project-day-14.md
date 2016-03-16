---
title: '프로젝트 14일: Heroku에 Node.js 애플리케이션 배포'
date: '2014-05-20'
layout: 'holiday-project'
image: 'http://hatemogi.com/img/holiday-project/heroku-logo.png'
url: 'holiday-project-day-14/'
description: '프로젝트 14일째, 오늘은 데모 웹사이트를 만들어 올리기 위해, (1) Node.js용 템플릿 엔진인 Jade를 잠깐 써보고, (2) Heroku에 express 앱을 배포해봤다. '
---

프로젝트 14일째, 오늘은 데모 웹사이트를 만들어 올리기 위해, (1) 노드용 템플릿 엔진인 [Jade]를 잠깐 써보고, (2) Heroku에 지금까지 만들고 있는 [express 웹앱](https://github.com/hatemogi/holiday-project)을 배포해봤다.

<!--more-->

Node.js 템플릿 엔진 - Jade
-----------------------

루비 쪽의 [Haml]과 같은 템플릿 엔진이다. 간결한 문법과 들여쓰기로, HTML/XML 태그를 여닫는 번거로움 없이 간편하게 결과 페이지를 만들 수 있다. 이하는 오늘 작성한 템플릿 레이아웃의 일부다.


### [layout.jade](https://github.com/hatemogi/holiday-project/blob/day-14/views/layout.jade)의 일부

```jade
doctype html
html
  head(prefix="og: http://ogp.me/ns#")
    meta(charset="utf-8")
    meta(name="viewport" content="width=device-width, initial-scale=1")
    title= title
    link(rel='stylesheet' href='/bower_components/bootstrap/dist/css/bootstrap.min.css')
    script(src='/bower_components/jquery/dist/jquery.min.js')
    script(src='/bower_components/bootstrap/dist/js/bootstrap.min.js')
  body
    block content
    include ga
    include github_badge
```

위와 같은 모습인데, HTML 문서의 태그를 들여쓰기로 어느 부분까지 감싸는 것인지 표현한다. 각종 태그를 그대로 쓰면 되고, 괄호 안의 속성값이 그대로 해당 element의 속성이 된다. 중간중간 현재 문맥에 바인딩 된 값을 가져다 쓸 수 있다. 위 레이아웃에서는 ```title``` 태그의 값이 그렇게 사용된다.

body 태그 바로 아래에 ```block content``` 부분이 실제 본문 내용이 대치/입력돼 최종 출력된다고 보면 된다. ```include```는 말 그대로 별도 템플릿을 읽는다.

템플릿 엔진이야 워낙 다양하고, 어떤 언어와 웹서비스 프레임워크를 쓰느냐 만큼 개인별 호불호가 달라서, 스스로 마음에 드는 템플릿 엔진을 골라서 사용하면 된다.

Heroku에 배포
-------------

프로젝트가 잘 진행되어, 웹 애플리케이션을 올려서 공개한다면, 아마도 [AWS EC2]나 [Google Compute Engine]에 직접 구축해서 올릴 테지만, 아직 그런 단계는 아니고, 이 블로그를 읽고 있는 소수의 지인께만 보여드리는 데모 웹페이지가 필요한 정도다. 

[Heroku]는 각종 웹 애플리케이션을 올려서 서비스할 수 있는 클라우드 서비스인데, 스타트업들이 즐겨 사용하는 플랫폼이다. 개인적으로는 [2012년에 Coursera에서 SaaS 수업](http://hatemogi.com/saas_class/)을 들으며 써본 적이 있는데, 좋은 인상을 받았던 서비스다.

로컬에서 잘 개발하고, heroku에 생성한 앱의 git 저장소 주소로 push하면 해당 애플리케이션이 배포되며, 곧바로 ```앱이름.herokuapp.com```에서 접근해 확인할 수 있다. 직접 서버를 설치하고 구동하고 관리할 필요없이 ```git push heroku```만으로 배포되고, 그 후 운영은 heroku가 알아서 한다. 

<img src="/img/holiday-project/heroku-dashboard.png" style="width: 600px;"/>

이번 데모를 위해서 최소 자원을 받았고, 개발용 Postgres 인스턴스도 add-on으로 붙인 화면이다. 비록 1만 레코드까지만 쓸 수 있는, 정말 개발시연용이지만, 지금의 목표에는 충분히 활용할 수 있다. 무엇보다도, 데모를 위한 규모에서는 **무료로 서비스를 이용**할 수 있다. 보이는가? 월 예상비용 $0.00 !!!

[개인적으로는 프로젝트 초기에는 그냥 간단히 가볍게 SQLite 활용하자는 마인드](https://twitter.com/hatemogi/status/399814963360849920)지만, Heroku 특성상, 일단 무료 Postgres를 써보기로 한다.

배포 중에 발생한 한가지 문제는, [프로젝트 4일 차에 알아본 Bower](/holiday-project-day-04/)로 가져다 쓴 Bootstrap과 jQuery를 Heroku에 배포한 앱에서는 설치되지 않았던 점이 있다. 프로젝트 배포하고 ```bower install```을 실행해야 하는데, Heroku에서 기본으로 그 커맨드를 실행할 이유는 없는 것. 검색을 좀 해보니, 무슨 Heroku의 빌드팩이라는 걸 만들어서 해결하는 방법이 있던데, 뭘 그렇게까지 해야 하나 싶어서 좀 더 검색해보니, [아주 간단하게 해결한 방법](http://xseignard.github.io/2013/02/18/use-bower-with-heroku/)을 찾을 수 있었다. 

```package.json```의 ```scripts``` 부분에 ```postinstall```이라는 항목에, 실행할 스크립트를 넣어 놓으면 ```npm install```이 진행되고 나서 이 스크립트를 실행하게 되는데, 이 부분을 이용하는 것.

```json
  "scripts": {
    "start": "node ./bin/www",
    "karma": "./node_modules/karma/bin/karma start",
    "spec": "./node_modules/jasmine-node/bin/jasmine-node --coffee --verbose spec",
    "postinstall": "./node_modules/bower/bin/bower install"
  },
```

이렇게 간단하게 웹서비스 실행 전에 ```bower install```을 실행할 수 있다. 한 가지 단점은, 이 커맨드를 실행하기 위해, (실제 실행 때에는 필요하지 않은) bower 의존성을 걸어야 한다는 점이 있으나, 무시할만하다. 

그렇게 해서, 결국 데모 앱을 준비했다. 아직 뭐 하는 기능은 없고, 단지 Heroku에 **텅 빈** express 앱을 띄워 놓은 것. 차차 여기에 살을 붙이기로 한다.

> <http://holiday-project.hatemogi.com/>

(위 주소에 heroku 서비스를 올려놓았으나, 아직은 방문하지 말아달라. ^^;)

한가지 전혀 다른 얘기로, Heroku add-on을 살펴보다가 Redis addon을 슬쩍 봤는데, 아래 캡처이미지에 보이듯, 3만8천 개 인스턴스를 서비스하고 있단다. 회사 업무로 우리 팀이 하는 일 중 하나가, 사내에 Redis 인스턴스 클라우드스럽게(?) 제공해주고 운영하는 일인데, 이거 외부에서 장사해도 될만한 일이었구나하는 뒤늦은 아쉬움이 든다.

<img src="/img/holiday-project/redis-to-go.png" style="width: 600px;"/>

3만8천 개라니... 대단하다. 장사 잘되는구나?!

오늘은 여기까지.

[Haml]: http://haml.info/
[Jade]: http://jade-lang.com/
[AWS EC2]: http://aws.amazon.com/ko/ec2/
[Google Compute Engine]: https://cloud.google.com/products/compute-engine/
[Heroku]: https://heroku.com/



