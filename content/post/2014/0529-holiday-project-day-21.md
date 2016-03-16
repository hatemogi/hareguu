---
title: '프로젝트 21일: AngularJS 컨트롤러 테스트'
kind: 'article'
date: '2014-05-29'
layout: 'holiday-project'
url: 'holiday-project-day-21/'
description: 'AngularJS 코드 스쿨 강의를 듣기 전에는 AngularJS는 조금 거창한 SPA (단일 페이지 웹 앱)에나 써볼 만한 거창한 프레임워크라고 생각했다. 강의를 듣고 나서야, SPA를 위해서도 좋지만, 멀티 페이지로 작성하더라도, 뷰와 로직을 잘 분리해 낼 수 있는 점과, 테스트를 잘할 수 있도록 준비돼 있다는 점이 훌륭하다는 것을 알게 됐다.'
---

이 글을 작성하는 지금은, 개인 프로젝트 23일째, 지난 21일째부터 정리하지 못한 밀린 숙제를 한다.

숙제가 밀린 이유
--------------

21일째에는 AngularJS 코드를 테스트하려는 노력을 기울이다가, 블로그로 정리해야 할, 밤늦은 시간에 회사 동료들을 만나러 나갔다. 합병 소식에 허전해서였을까? 같은 단지에 사는 후배이자 동료를 불러봤는데, 마침 나올 수 있다고 했고, 그렇게 한 명 두 명 즉흥적으로 불러봤는데, 결국 5명이 모여서 늦은 시간까지 시끄럽게 떠들었다.

<img src="/img/post/holiday-project/kcircle_hagun_jhban.jpg" style="width: 400px;"/>

한 명 더 왔었는데, 그가 왔을 때 이미 난 사진을 찍을 만한 상황이 아니었던지라, 이 사진이 전부다. 사진 잘 나왔다. 모두 다음의 탑클래스 개발자들이다. 치맥을 먹으면서도 ```git stash```와 Scala의 immutable 변수 ```val```을 논하는 천상 개발자들이다.

아무튼 즐거운 시간을 보내고 집에 돌아왔는데, 만취 상태라서 프로젝트 기록을 남기기는커녕 다음 날까지도 숙취에 정신을 못 차리고, 이제야 뒤늦은 숙제를 다시 시작했다.


AngularJS 테스트 코드
--------------------

AngularJS 코드 스쿨 강의를 듣기 전에는 AngularJS는 조금 거창한 SPA (단일 페이지 웹 앱)에나 써볼 만한 거창한 프레임워크라고 생각했다. 강의를 듣고 나서야, SPA를 위해서도 좋지만, 멀티 페이지로 작성하더라도, 뷰와 로직을 잘 분리해 낼 수 있는 점과, 테스트를 잘할 수 있도록 준비돼 있다는 점이 훌륭하다는 것을 알게 됐다. 아예 템플릿 프로젝트 [angular-seed](https://github.com/angular/angular-seed)에 테스트 코드가 작성된 예제가 있다.

그 예제 코드와 문서를 조금 살펴보고 나서 간단히나마 컨트롤러 테스트케이스를 작성할 수 있었다.

### [angular_spec.js](https://github.com/hatemogi/holiday-project/blob/day-21/public/spec/angular_spec.js)
```js
describe('angular 컨텍스트', function() {
  beforeEach(module("holiday"));
  it("버전 확인", inject(function($controller) {
    expect(angular.version).toBeDefined();
    expect(angular.version.full).toEqual("1.3.0-beta.8");
    console.log($controller("LoginCtrl"));
    expect($controller("LoginCtrl").welcome).toEqual("환영합니다");
  }));
});
```

일반적인 Jasmine 스펙 코드와 다른 점은 ```beforeEach```에 있는 ```module```이라는 함수와, ```it```에 걸려있는 ```inject```라는 함수다. 이 둘 다, [angular-mocks](https://github.com/angular/bower-angular-mocks) 모듈이 제공해주는 함수로, 테스트를 위해 사용한다.

```module(모듈명)``` 함수는, 현재 Karma 환경에 로드된 AngularJS 앱을 기준으로 모듈을 찾아서 이하 테스트 케이스를 실행할 때 준비해준다. ```inject``` 함수는 콜백에서 ```$controller``` 파라미터를 받아서, 특정 컨트롤러를 받아올 수 있게 한다.

즉, ```$controller("LoginCtrl").welcome```은, [app.js.coffee](https://github.com/hatemogi/holiday-project/blob/day-21/public/assets/app.js.coffee)에 선언한 컨트롤러의 welcome 속성을 읽는다.

컨트롤러뿐 아니라, 필터나 디렉티브에 대한 테스트 케이스도 [angular-seed 프로젝트에서 예제](https://github.com/angular/angular-seed/tree/master/test/unit)를 볼 수 있다.

사실 테스트를 자동화한다는 것이 종종 생각만큼 쉽지 않고, 어떤 부분에서는 포기하게 되기 쉬운데, AngularJS가 이 정도로 친절하게 준비해주었으니, 그전보다는 공격적으로 더 많은 부분에서 테스트를 자동화해놓을 수 있겠다.

Promise와 Async
----------------

AngularJS의 컨트롤러 스펙을 간단히 작성해보고 뿌듯한 마음으로, [20일째 조금 알아본 Promise](/holiday-project-day-20/)에 대해서도 더 알아보기 시작했다. [10일째 콜백 중첩을 풀기 위해 사용했던 Async.js](holiday-project-day-10/)와 비교해보고 싶었다. 어떨 때 어떻게 어느 것을 사용하면 좋을지 궁금했던 것인데, 둘 다 작성해보고 결국 마음에 드는 결론을 얻은 것은 프로젝트 22일째였다. 22일째 기록에 해당 내용을 정리하기로 한다.

오늘은 여기까지.