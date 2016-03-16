---
title: '프로젝트 6일: 그래프 그리기 - graphviz & D3.js'
kind: article
date: 2014-05-12
layout: holiday-project-d3js
url: holiday-project-day-06
---

나는 30일간의 안식휴가 중이다. 휴가 동안 개인 개발 프로젝트를 진행하고 있고, 오늘은 그 엿새째로, 웹 페이지에서 그래프를 그려서 보여주는 방법을 알아보려 한다.

### [graphviz]

예전에, 그래프를 그리는 소프트웨어로 [graphviz]를 간단히 써봤었다. 그래프를 [dot]이라는 도메인 특화 언어(DSL)로 작성하고 명령어를 실행하면, png, pdf, svg 파일로 시각화해서 보여준다.

예를 들어, 아래와 같은 ```sample.dot``` 파일을 준비하고,

##### sample.dot
```dot
digraph G {
  node [style=filled, fillcolor=steelblue, fontsize=10];
  가 -> 나;
  가 -> 다;
  다 -> 나 [constraint=false];
}
```

아래의 ```dot 명령어```로 SVG파일을 만들 수 있다.

```bash
dot -Tsvg < sample.dot > sample.svg
```

결과 SVG의 모습은 아래와 같다.

##### sample.svg

[graphviz]: http://www.graphviz.org/
[dot]: http://www.graphviz.org/content/dot-language

<svg width="134pt" height="116pt"
 viewBox="0.00 0.00 134.00 116.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
<g id="graph0" class="graph" transform="scale(1 1) rotate(0) translate(4 112)">
<title>G</title>
<polygon fill="white" stroke="none" points="-4,4 -4,-112 130,-112 130,4 -4,4"/>
<!-- 가 -->
<g id="node1" class="node"><title>가</title>
<ellipse fill="steelblue" stroke="black" cx="63" cy="-90" rx="27" ry="18"/>
<text text-anchor="middle" x="63" y="-87.9375" font-family="Times,serif" font-size="10.00">가</text>
</g>
<!-- 나 -->
<g id="node2" class="node"><title>나</title>
<ellipse fill="steelblue" stroke="black" cx="27" cy="-18" rx="27" ry="18"/>
<text text-anchor="middle" x="27" y="-15.9375" font-family="Times,serif" font-size="10.00">나</text>
</g>
<!-- 가&#45;&gt;나 -->
<g id="edge1" class="edge"><title>가&#45;&gt;나</title>
<path fill="none" stroke="black" d="M54.6504,-72.7646C50.2885,-64.2831 44.8531,-53.7144 39.9587,-44.1974"/>
<polygon fill="black" stroke="black" points="42.9904,-42.4395 35.3043,-35.1473 36.7654,-45.6409 42.9904,-42.4395"/>
</g>
<!-- 다 -->
<g id="node3" class="node"><title>다</title>
<ellipse fill="steelblue" stroke="black" cx="99" cy="-18" rx="27" ry="18"/>
<text text-anchor="middle" x="99" y="-15.9375" font-family="Times,serif" font-size="10.00">다</text>
</g>
<!-- 가&#45;&gt;다 -->
<g id="edge2" class="edge"><title>가&#45;&gt;다</title>
<path fill="none" stroke="black" d="M71.3496,-72.7646C75.7115,-64.2831 81.1469,-53.7144 86.0413,-44.1974"/>
<polygon fill="black" stroke="black" points="89.2346,-45.6409 90.6957,-35.1473 83.0096,-42.4395 89.2346,-45.6409"/>
</g>
<!-- 다&#45;&gt;나 -->
<g id="edge3" class="edge"><title>다&#45;&gt;나</title>
<path fill="none" stroke="black" d="M71.9297,-18C69.3149,-18 66.7002,-18 64.0854,-18"/>
<polygon fill="black" stroke="black" points="64,-14.5001 54,-18 64,-21.5001 64,-14.5001"/>
</g>
</g>
</svg>

훌륭하지 않나? node의 위치나 edge를 연결하는 수고는 graphviz가 다 알아서 해주고, 이용자는 논리적인 관계와 약간의 스타일만 지정해주면 된다. 물론, 저 DOT보다 훨씬 복잡한 파일도 멋지게 렌더링해낸다. 그래프로 표현하면 보기 좋은, 소프트웨어 의존성(dependency) 그래프를 그리는 데 활용하기도 좋다. [갤러리](http://www.graphviz.org/Gallery.php)에서 멋진 예들을 감상할 수 있다.

[graphviz]를 써서 프로젝트에서 활용하고 싶은 기능은, 해당 그래프로 보이고, 특정 그래프 노드에 웹 링크(href)를 걸어서 다른 페이지로 이동하거나 특정 행위를 일으키고자 하는 기능이다. 관련해서, png로 출력한다면 링크를 따로 걸기 힘들겠지만, svg라면 가능할 것 같아서, 해당 기능을 알아보려고 구글링을 하던 차, 전혀 상관없는 [D3.js]를 발견했다.

음, 이건 뭐하는 거지? 궁금해서 찾아보려는데, 일단 [사이트에 있는 예제](https://github.com/mbostock/d3/wiki/Gallery)들의 비주얼이 너무도 멋지다. 이내 화려한 비주얼에 현혹되어, 원래 알아보고자 했던 "graphviz로 SVG를 만들 때 노드의 링크 걸기"는 머릿속에서 사라져 버리고, 이미 이 [D3.js]가 뭐하는 소프트웨어이고, 내가 어떻게 써 볼 수 있는지 알아보고 있었다.


### [D3.js]

> D3.js is a JavaScript library for manipulating documents based on data. D3 helps you bring data to life using HTML, SVG and CSS. D3’s emphasis on web standards gives you the full capabilities of modern browsers without tying yourself to a proprietary framework, combining powerful visualization components and a data-driven approach to DOM manipulation.

사이트에 소개된 내용으로는 자바스크립트로 데이터를 시각화하는 라이브러리인데, 이미 잘(?) 쓰이고 있는 HTML, SVG, CSS를 활용해서 시각화한다. jQuery와 비슷한 언어로 각종 연산을 할 수 있다. graphviz는 그래프의 상관관계에 집중해서 그려내는 데 좋은 소프트웨어이고, D3.js는 수치 데이터를 중심으로 그래프(node, edge)뿐만 아니라 다양한 그래픽으로 렌더링하기에 좋은 소프트웨어이다. 그리고 자바스크립트로 제어하기 때문에, 서버를 거치지 않고 브라우저에서만도 별도의 처리가 가능하다. (좀 찾아보니, graphviz도 누가 emscripten으로 컴파일해서 쓰기도 하던데, 그런 경우는 논외로 하자.)

내 프로젝트에서 쓰고자 하는 기능은 제한된 그래프 시각화만 하면 되기 때문에, 굳이 이렇게 화려한 툴을 써야 할까 하는 의문이 들었지만, 조금 전에 적었다시피, 이미 비주얼에 현혹돼서, 더 알아보고야 말았다. (게다가, [어떤 멋진 분이 한국어로 일부 문서를 번역](https://github.com/zziuni/d3/wiki)해 두셔서, 더 기쁜 마음으로 살펴보게 됐다.)

나름, 오늘 있는 시간 거의 다 써서 알아봤으나, D3.js를 한나절 만에 이해하기에는 내 능력이 미치지 못했지만, [D3.js]에 대한 호감은 강하게 자리 잡았다. 오늘 한나절의 노력 끝에, 예제를 따라해가며, 조금씩 이해하며 그려낸 그래프는 아래에 첨부한다.

#### 한나절 내내 그린 그래프

[D3.js]: http://d3js.org/

<style>
  circle {
    stroke: black;
    stroke-width: 1.5px;
    fill: steelblue;
  }
  text {
    stroke: white;
    text-anchor: middle;
  }
  path {
    stroke: darkgrey;
    stroke-width: 2px;
    fill: none;
  }
</style>
<svg class="d3"></svg>

<script>
  var width = 500;
  var height = 300;
  var points = [[50, 140], [200, 40], [400, 230]];
  var textVal = ["가", "나", "다"];

  var x = d3.scale.linear().range([0, width]),
      y = d3.scale.linear().range([height, 0]);

  var chart = d3.select("svg.d3")
    .attr("width", width)
    .attr("height", height)

  var g = chart.selectAll("g")
    .data(points)
    .enter();

  var line = d3.svg.line()
    .interpolate("monotone");

  var path = chart.append("path")
    .attr("class", "line")
    .attr("d", line(points));

  var group = g.append("g")
    .attr("transform", function(d, i) {
      return "translate(" + d[0] + ", " + d[1] + ")";
    });

  var circle = group.append("circle").attr("r", 30);

  var text = group.append("text")
    .attr("dy", ".35em")
    .text(function(d, i) { return textVal[i]; });
</script>

#### 위 그래프를 그리는 D3.js 소스코드 조각

```html
 <style>
   circle {
     stroke: black;
     stroke-width: 1.5px;
     fill: steelblue;
   }

   text {
     stroke: white;
     text-anchor: middle;
   }

   path {
     stroke: darkgrey;
     stroke-width: 2px;
     fill: none;
   }
 </style>
 <script>
    var width = 500;
    var height = 300;
    var points = [[50, 140], [200, 40], [400, 230]];
    var textVal = ["가", "나", "다"];

    var x = d3.scale.linear().range([0, width]),
        y = d3.scale.linear().range([height, 0]);

    var chart = d3.select("svg.d3")
      .attr("width", width)
      .attr("height", height)

    var g = chart.selectAll("g")
      .data(points)
      .enter();

    var line = d3.svg.line()
      .interpolate("monotone");

    var path = chart.append("path")
      .attr("class", "line")
      .attr("d", line(points));

    var group = g.append("g")
      .attr("transform", function(d, i) {
        return "translate(" + d[0] + ", " + d[1] + ")";
      });

    var circle = group.append("circle").attr("r", 30);

    var text = group.append("text")
      .attr("dy", ".35em")
      .text(function(d, i) { return textVal[i]; });

 </script>
```

비슷한 그래프를 그리기에는 graphviz로 그렸던 것보다 훨씬 더 수고스러워지긴 했지만, D3.js의 강점은 다른 데서 드러난다. (게다가 해결하고자 하는 문제가 엄연히 다른 소프트웨어이므로 동일선상 비교는 의미가 없다.)

코드를 보면 눈치챌 수 있듯이, 평소 쓰는 CSS처럼, 스타일을 지정할 수 있고, jQuery와 비슷한 형태의 함수로 SVG 요소(element)를 만들고 속성을 지정할 수 있다. 무엇보다, 어떤 데이터를 가져다가 원하는 인포그래픽으로 보여줄 수 있으며, 심지어 각종 트랜지션을 비롯한 애니메이션도 곁들일 수 있는 매력적인 자바스크립트 라이브러리다.

아직 이해가 너무 부족하고, 이번 프로젝트에 쓰기에 적합한지 아닌지도 아직 확실치 않지만, 오늘은 이 정도 첫인상을 받은 것만으로도 만족하고 마무리한다.

이미 밤이 깊었다. 오늘은 여기까지.

