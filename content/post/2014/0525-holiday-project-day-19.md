---
title: '프로젝트 19일: RequireJS - 비동기(async) JS 로더'
kind: 'article'
date: '2014-05-25'
layout: 'holiday-project'
image: http://hatemogi.com/img/holiday-project/requirejs-logo.png
url: 'holiday-project-day-19/'
description: '자바스크립트 파일을 비동기 방식으로 읽어들이고, 각 파일의 의존도를 따라서 필요한 파일을 먼저 읽어 들일 수 있게 해주는 RequireJS를 알아보고 적용해보았다. '
---

개인 프로젝트 19일째, 오늘은 비동기식 자바스크립트 로더, [RequireJS]에 대해 살펴봤다.

<img src="/img/holiday-project/requirejs-logo.png" style="width: 200px;"/>

비동기 모듈 선언 (AMD)
---------------------

오늘 고생된 작업의 시작은, 오전에 지금까지 진행한 프로젝트 데모 사이트에서, 크롬 브라우저의 [PageSpeed]를 돌려본 것이 화근이었다.  웹페이지 로딩 속도에 관련한 각종 검사를 하고 개선할 방안을 추천해주는 [PageSpeed]가 내게 권장하기를,

> 자바스크립트 파일들을 비동기식으로 로딩하시죠~!

라고 하는 게 아닌가?!

음, 비동기식 로딩이라... ```<body>``` 태그의 가장 마지막에 자바스크립트 파일들을 로딩하면 되는건가? 아니면, ```<script>``` 태그에 ```async``` 속성을 주면 되는 건가?

조금 찾아보니, [AMD]라고 비동기 모듈 선언(Asynchronous Module Definition)이라는 주제를 찾을 수 있었다.

![https://en.wikipedia.org/wiki/File:Asynchronous_Module_Definition_overview.png](/img/holiday-project/AMD_overview.png)

쉽게 말해서, 여러 JS 파일들을 하나씩 차례로 읽지 말고, 동시다발적으로 읽자는 얘기고, 그러면 당연히 여러 파일을 전부 다 읽는 데 필요한 시간이 줄어들 수 있다는 얘기다.

[이걸 왜 써야 하는지 아주 상세한 문서](http://requirejs.org/docs/whyamd.html)가 있는데, 상세한 만큼, 내용도 길다.

> 그냥 HTML 문서에 ```<script>``` 태그 넣으면 되는 것을, 이 장문의 영문을 읽고 해석하고 적용해야 하는 걸까?

하는 불평이 스멀스멀 올라왔지만, "그래 어디 한번 써보기나 합시다"는 마음으로 적용해보기로 했다. AMD를 위한 라이브러리가 꽤 여럿 있는 것 같은데, 그중에서도 가장 눈에 띈 [RequireJS]를 써보기로 했다.

일단 아주 긴 문서 길이에 살짝 위축됐고, AngularJS나 Karma와의 궁합이 잘 안 맞을 수 있으니, 자칫하면 취소하려는 마음으로 브랜치부터 땄다. 지금까지의 ```master``` 브랜치에서 ```require.js``` 브랜치를 땄다.

```bash
git checkout -b require.js
```

RequireJS 적용 시도
--------------------

문서를 보며 따라 해보니, 기본 개념은 아래 코드가 핵심이었다.

```javascript
define('모듈ID', ['필요한모듈1', '필요한모듈2'], function(모듈1, 모듈2) {
  // 모듈1, 모듈2를 사용해서 무언가를 하는 코드
});
```

특정 모듈을 ```define``` 이라는 함수로 선언한 수 있고, 그 모듈은 ```필요한모듈1```과 ```필요한모듈2```를 필요로 하는 상황이다. 이 모듈을 쓰고자 한다면, RequireJS가 그 모듈들을 로드한 뒤에 이 모듈 선언 함수 부분을 실행한다. 즉, 의존성을 쫓아 로드할 수 있다.

비동기로 동시다발적으로 로드하고, 의존성이 있는 경우에는, 그 의존하는 모듈이 로드된 다음에 해당 코드를 실행할 수 있으므로, 로드 순서에 따른 문제도 피할 수 있고, 전체 로딩 시간은 짧아지는 효과를 노리는 것이다.

게다가, 선언만 해놓고, 실제로 쓰지 않는다면, 불필요하게 읽지 않으므로, 웹사이트의 전반적인 속도가 빠르게 느껴질 수도 있다. 예를 들어, 웹사이트 일부에 ```D3.js```를 쓰는 경우, 의존성 선언을 해놓고, 필요한 부분에서만 디펜던시를 걸어서 사용하면, 꼭 필요한 경우에만 로딩되게 할 수 있다.

개념이며 목표며, 다 좋은데, 실제로 적용해보려니, Karma와 같이 쓰기가 쉽지 않았다. [Karma 사이트에 RequireJS와 함께 쓰는 법을 알려주는 문서](http://karma-runner.github.io/0.8/plus/RequireJS.html)가 있었지만, 아직 이해가 부족해서인지 따라해 봐도 쉽게 해결할 수 없었다.

한나절 전부를 써서, 결국 간단히 Karma로도 테스트를 수행하고, 웹 브라우저에서도 잘 동작하는 코드를 작성할 수 있었는데, 그래도 아직 뭔가 부족하다. 아직 AngularJS의 컨트롤러 등을 테스트하는 코드를 깔끔하게 작성하지 못한 상태다.

AngularJS는 별도 의존성이 없으므로,  차라리 그냥 동기 방식으로 미리 로딩하고, 나머지만 RequireJS에 묶는 게 편할지도 모르겠다. 계속 써볼지 말지 아직은 불투명한 상황. 내일이라도 지금 작업하는 브랜치를 그냥 지워버릴지도... ㅠ.ㅠ

우선, RequireJS를 적용한 브랜치는 아래 주소에 있다.

> <https://github.com/hatemogi/holiday-project/tree/require.js>

[어제](/holiday-project-day-18) 알아본 Grunt로 ```karma``` 태스크를 넣어두었다.

```bash
grunt karma
```

간신히, 기본적으로 돌아가게는 했으나, 아직 더 자세히 알아보며 써봐야 할 단계다. 이해가 부족하다.

새로운 웹 개발 기술들
------------------

무엇 하나 개발하기에도 바쁜 데, 늘 따라가기 힘든 새로운 기술들이 쏟아져 나온다. 하도 많이 나와서, 다 알아보기도 힘들고, 또 대다수는 조금 쓰이다가, 버려지는 것들도 있어서 남들이 많이 쓰기 전까지는 기다리는 것이 현명한 판단일 수도 있다.

하지만, 그렇다고 늘 하던 대로만 하면, 나아지는 것이 없다.

나름의 내 정체성은 (아직) 웹 개발자인데, 너무 모르고 지나가는 기술들이 많았었나 보다. 모르는 기술들을 뒤늦게 살펴보고 있으니,  정작 **실제** 개발작업은 더뎌져서 답답하기도 한데, 그래도 당장 현업의 개발 업무가 눈앞에 있다면, 이렇게 모르는 기술 알아볼 시간도 없이 개발했을텐데, 이번 기회에 여유롭게(?) 이것저것 다양히 알아보고 있으니, 그것참 사치스러운 시간이다.

오늘은 여기까지.

[AMD]: https://en.wikipedia.org/wiki/Asynchronous_module_definition
[RequireJS]: http://requirejs.org/
[PageSpeed]: https://chrome.google.com/webstore/detail/pagespeed-insights-by-goo/gplegfbjlmmehdoakndmohflojccocli?hl=ko