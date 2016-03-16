---
title: '프로젝트 17일: Travis CI, 지속적 통합 서비스'
kind: 'article'
date: '2014-05-23'
layout: 'holiday-project'
image: 'http://hatemogi.com/img/holiday-project/travis-logo.png'
url: 'holiday-project-day-17/'
description: 'TravisCI, GitHub과 연동되는 지속적 통합 서비스'
---

개인 프로젝트 17일째, 오늘은 지속적 통합 서비스인 [Travis CI]를 살펴보고, 개인 프로젝트에 적용해봤다.

Travis CI 소개
-------------

[Travis CI]는 GitHub과 연동해 [지속적 통합(Continuous Integration)](https://en.wikipedia.org/wiki/Continuous_integration)을 호스팅해주는 서비스다.

지속적 통합의 정확한 의미는 조금 더 넓을 수 있지만, [Travis CI]로 한정해서 쉽게 생각하자면, GitHub 저장소에 새로운 커밋이 push되었을 때 CI 서버가 뒤에서 자동으로 새로운 커밋을 가져와서 빌드 테스트를 수행하고, 그 결과를 리포팅 해주는 서비스다.

> C, C++, Clojure, Erlang, Go, Groovy, Haskell, Java, JavaScript (Node.js), Objective-C, Perl, PHP, Python, Ruby, Scala

이상의 다양한 언어 환경에서의 빌드 테스트를 대행해주고, 더불어 다양한 데이터스토어와 메시지 브로커 같은 툴들도 미리 설치돼 있어서, 테스트를 위해 활용할 수 있다.

다른 언어도 비슷하겠지만, Node.js의 경우에도 버전을 명시해서 여러 버전에 대해 동시에 테스트를 맡길 수도 있다. 예를 들어, 0.10.x 버전과 0.8.x 버전 모두에서 테스트하고 결과를 확인하는 것이 가능하다.

[Travis CI] 사이트에 GitHub 계정으로 로그인할 수 있고, 연동된 기능을 통해 로그인한 계정의 저장소 목록을 열람하고, 웹 훅(Web Hook)을 간단히 설정할 수 있다.

웹 훅이 설정되고 나서 GitHub 저장소에 새로운 커밋이 들어오면, [Travis CI]가 바로 알게 되고, 설정에 따라 빌드 테스트를 수행한다. 설정을 위해서는 저장소에 ```.travis.yml```이라는 YAML 파일을 넣어 두면 된다. Node.js용 설정 파일의 예는 아래와 같다.

```yaml
 language: node_js
 node_js:
   - "0.10"
   - "0.11"
```

이렇게 설정한 프로젝트의 경우, 0.10.x대의 최신 버전과, 0.11.x대의 최신 버전으로 빌드 테스트를 진행한다.

Travis CI 적용
-------------

현재 진행하는 프로젝트의 [GitHub 저장소](https://github.com/hatemogi/holiday-project/)에 Travis CI를 적용했다.

### [프로젝트 현재 상태](https://travis-ci.org/hatemogi/holiday-project/builds/25843181)
<img src="/img/holiday-project/travis-current-status.png" style="width: 600px;"/>

간단히 적절한 설정을 한 뒤, 저장소에 ```git push```를 하고 나면, Travis CI가 테스트를 수행하고, 위와 같은 [결과 페이지](https://travis-ci.org/hatemogi/holiday-project/builds/25843181)를 볼 수 있다.

### 빌드 테스트 결과 로그
<img src="/img/holiday-project/travis-test-result.png" style="width: 600px;"/>

상세한 빌드 로그도 살펴보고 무엇이 잘되고 잘못됐는지도 자세히 파악할 수 있다.

### [GitHub 저장소 README](https://github.com/hatemogi/holiday-project#readme)
<img src="/img/holiday-project/travis-build-status-img.png" style="width: 600px;"/>

최종 빌드 테스트의 성패에 따른 이미지 링크도 있어서, 저장소의 README파일에 링크를 걸어두면, 위와 같이 성공했을 경우, ```build passing```이라는 초록 버튼이 보이게 할 수 있다. 그리고, 해당 이미지 링크에 ```branch``` 파라미터를 줘서 특정 Git 브랜치의 최종 커밋에 대한 이미지도 보일 수 있다.

Heroku에 자동배포
-----------------

빌드 테스트가 성공하면, 스테이징 서버에 자동으로 배포한다면 어떨까? 좀 더 과감한 접근이 가능한 경우라면, 아예, 빌드 테스트 성공 후에는 실 서비스 서버에 배포하게 하는 것도 가능하겠다. 더구나 지금처럼 가벼운 개인 프로젝트의 경우, 빌드 테스트 성공 후에는 그때그때 바로 배포하면 매우 편리할 것이다. 하지만, 우린 사려깊은 개발자이니, 우선 스테이징 서버에 자동으로 배포해보자.

[Travis CI]의 [Heroku Deployment](http://docs.travis-ci.com/user/deployment/heroku/)라는 문서에 해당 내용이 친절히 안내돼 있다. 아래처럼 [travis CLI](https://github.com/travis-ci/travis.rb#readme)로 설정을 자동으로 추가할 수 있고,

```bash
travis setup heroku
```

바뀐 [.travis.yml]의 내용을 보면, travis CLI가 추가한 부분은 아래와 같다.

### [.travis.yml]
```yaml
deploy:
  provider: heroku
  api_key:
    secure: bbxlxzhB4AedhkiqKVnagzWVWpe+kusUbeYZhRjL1BJfzvGO0zXktzyqNvy6wANSetDgSMQL/wZsPkIcbtDFU1QzsvXxrfmc33TMFARCQ6JJpb+EHD31HluOwA/8nbtsqQsz8UXW+7jKjtuAxpo0ZzLE7KI0ckQRUKIRQAcky+I=
  app: holiday-project-staging
  on:
    repo: hatemogi/holiday-project
```

중간에 ```api_key``` 부분이 Heroku에 배포하기 위한 Heroku API 키를 Travis CI만 해석할 수 있게 암호화한 값이다.

이렇게 설정을 마치고, GitHub에 새로운 커밋을 push 했더니, 문제없이 빌드 테스트를 마쳤고, Heroku에도 자동으로 배포됐다.

[14일째, 수동으로 Heroku에 배포한](/holiday-project-day-14/) 실 서비스(?) 주소와 오늘부터 Travis CI가 자동 배포하는 스테이징 서버의 주소는 아래와 같다.

> * (실 서비스) <http://holiday-project.hatemogi.com/>
> * (스테이징) <http://holiday-project-staging.herokuapp.com/>

아직 둘의 차이는 없지만, 개발 과정에서 종종 차이가 있을 것이고, 실 서비스 배포 전에, 로컬 테스트와 더불어 스테이징 테스트까지도 아주 편리하게 할 수 있겠다. 그것참 개발하기 편한 세상이다.

이로써, 그간 살펴보려고 모아두었던 기술 주제들은 대략적으로나마 알아보았다. 내일부터는, 본격(?) 개발 작업에 들어가려 한다.

오늘은 여기까지.

[Travis CI]: https://travis-ci.org
[지속적 통합]: https://en.wikipedia.org/wiki/Continuous_integration
[.travis.yml]: https://github.com/hatemogi/holiday-project/blob/day-17/.travis.yml
