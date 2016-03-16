---
title: '프로젝트 18일: Grunt - 자바스크립트 작업 실행기'
kind: 'article'
date: '2014-05-24'
layout: 'holiday-project'
image: http://gruntjs.com/img/grunt-logo.png
url: 'holiday-project-day-18/'
description: '개인 프로젝트 18일째, 오늘은 자바스크립트 작업 실행기(task runner), Grunt를 프로젝트에 적용했다. 자동화 테스트를 실행하거나, 자바스크립트 파일들을 합치거나 최소화하는 일처럼 자주 반복하는 프로젝트 수반 작업을 편리하게 실행할 수 있게 해주는 도구다. '
---

개인 프로젝트 18일째, 오늘은 자바스크립트 작업 실행기(task runner), [Grunt]를 프로젝트에 적용했다. 자동화 테스트를 실행하거나, 자바스크립트 파일들을 합치거나 최소화하는 일처럼 자주 반복하는 프로젝트 수반 작업을 편리하게 실행할 수 있게 해주는 도구다.

Node 환경에서는, npm만으로도 충분할 수 있는데, [Grunt]에 몇 가지 마음에 드는 [플러그인]이 있어서, 곧바로 적용해 보기로 했다.

우선 npm으로 간단히 Grunt를 설치한다.

```
npm install -g grunt
```

Gruntfile 기본
--------------

Make를 쓸 때 Makefile이나 Rake를 쓸 때 Rakefile이 있듯, Grunt를 위해서는 ```Gruntfile.js```가 있다. 프로젝트 디렉터리에 있는 이 파일을 기준으로 각종 작업을 실행한다.

### Gruntfile.js 예제
```js
module.exports = function(grunt) {
  // 작업을 위한 설정
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    uglify: {
      build: {
        src: 'src/app.js',
        dest: 'build/app.min.js'
      }
    }
  });

  // "uglify" 작업을 위한 플러그인 등록
  grunt.loadNpmTasks('grunt-contrib-uglify');

  // 기본 작업에 'uglify' 등록
  grunt.registerTask('default', ['uglify']);
};
```

위와 같은 ```Gruntfile.js```를 프로젝트 최상위 디렉터리에 두고,

```bash
grunt
```

위의 명령을 실행하면, 아무 작업 이름을 주지 않았으므로 ```default``` 작업을 실행하려고 하며, 위 ```Gruntfile.js```의 최하단에 명시한 대로 ```uglify``` 작업을 진행한다. 그러면, [uglify 플러그인](https://github.com/gruntjs/grunt-contrib-uglify)로 등록한 작업을 한다.

즉, 위 예제의 경우

```bash
grunt uglify
```

라고 실행해도 결과는 같다. 참고로 ```Gruntfile.js``` 대신 ```Gruntfile.coffee``` 파일을 준비해 둬도 그 역할은 같으며, 단지 자바스크립트 대신 커피스크립트 문법으로 해석한다.

Grunt 플러그인 추가
------------------

[플러그인]이 꽤 많았고, 그중 아래의 플러그인을 골라 설치했다.

* [grunt-contrib-coffee]: 커피스크립트 컴파일
* [grunt-contrib-uglify]: Ugilfy로 자바스크립트 파일 최소화
* [grunt-contrib-watch]: 특정 파일들 감시하다가, 변경되면 지정한 작업 수행
* [grunt-contrib-jshint]: JSHint로 자바스크립트 파일 검증

이하는, 오늘 작성한 ```Gruntfile.coffee``` 파일의 일부다.

### [Gruntfile.coffee](https://github.com/hatemogi/holiday-project/blob/day-18/Gruntfile.coffee)
```coffeescript
module.exports = (grunt) ->
  js_files = ['app.js', 'lib/*.js', 'public/js/app.js']

  grunt.initConfig {
    # 중간 생략
    watch:  {
      coffee: {
        files: ["public/assets/*.coffee"]
        tasks: ["coffee", "uglify"]
      }
      jshint: {
        files: js_files
        tasks: ["jshint"]
      }
    }

    jshint: {
      all: js_files
    }
  }
  # 중간 생략
  grunt.registerTask 'default', ['coffee', 'uglify']
```

기본 작업에는 ```coffee```와 ```uglify```를 걸어 두었고, 별도로 ```watch``` 작업을 추가해서,

```bash
grunt watch
```

를 실행해 두고, 커피스크립트 소스파일을 편집하면, 그 후 저장할 때마다, 즉시 커피스크립트 컴파일하며, jshint로도 바로 검사한다. 더 나아가, 셀프 테스팅 코드도 ```watch```에 걸어두면, 그때그때 테스트 결과도 바로 확인할 수 있겠다.

Grunt, 쓸만한가?
---------------

npm의 script 섹션을 써도 웬만큼 다 되는데, 굳이 수고를 들여 별도의 작업 실행기가 필요할까? 잠깐 살펴보니, 그래도 될 거 같다. 우선 그 약간의 수고가 크지 않게 잘 만들어져있고, 문서화도 잘 돼 있다. 무엇보다, 플러그인이 많아서, 원하는 기능을 손쉽게 가져다 쓸 수 있다.

그리고, 참고로, [Grunt (비공식) 한글 사이트](http://gruntjs-kr.herokuapp.com/)도 있으니, 한글 문서로도 살펴볼 수 있어서 좋다.

오늘 작업을 포함해, 지금까지 진행하고 있는 내용을 GitHub에 올려놓고 있다.

> <https://github.com/hatemogi/holiday-project>

진행하는 내용은, [어제 살펴본 Travis CI](/holiday-project-day-17/)로 지속적으로 통합되며, [Heroku](/holiday-project-day-14/)에 이하 주소로 자동 스테이징되고 있다.

> <https://holiday-project-staging.herokuapp.com>

오늘은 여기까지.

[Grunt]: http://gruntjs.com/
[CoffeeLint]: http://www.coffeelint.org/
[grunt plug-ins]: https://github.com/gruntjs
[플러그인]: https://github.com/gruntjs
[grunt-contrib-coffee]: https://github.com/gruntjs/grunt-contrib-coffee
[grunt-contrib-uglify]: https://github.com/gruntjs/grunt-contrib-uglify
[grunt-contrib-watch]: https://github.com/gruntjs/grunt-contrib-watch
[grunt-contrib-jshint]: https://github.com/gruntjs/grunt-contrib-jshint
