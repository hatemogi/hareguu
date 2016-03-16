---
title: '프로젝트 7일:  graphviz 노드에 웹 링크 걸기'
kind: article
date: 2014-05-13
layout: holiday-project
url: holiday-project-day-07
---

나는 30일간의 안식휴가 중이다. 휴가 동안 개인 개발 프로젝트를 진행하고 있다.

원래는 [어제] 해결하려다, 다른 주제 [D3.js]에 마음을 빼앗겨 해결하지 못했던 문제로 돌아왔다.

> [graphviz]의 노드에 웹 링크(href)를 걸기

찾아보니 싱겁게도, [설명서의 각종 속성을 지정하는 부분에 너무도 간단히, 그것도 아주 뻔한 ```href```라는 속성으로 지정할 수 있다](http://www.graphviz.org/content/attrs#dhref)고 쓰여있었다. 지정할 수 있는 속성이 너무 많아서 일일이 살펴보기 어렵다는 점이 있다고 핑계를 대고 싶지만, 아, 너무도 뻔한 속성값이다.

어쨌든, 한번 연습 삼아서 링크를 걸어서 graphviz로 svg 그래프 하나 그려보는 걸로 일단락하기로 한다. 주제는, 오늘까지 7일간 살펴본 기술들의 의존성 그래프 그리기다.

### day-07.dot
```dot
digraph G {
  rankdir="LR";
  splines="curved";
  node [style=filled, fillcolor=steelblue, fontsize=9, fontcolor=white];
  edge [arrowsize=0.7];

  시작 [fillcolor=deepskyblue, shape="rect", style="filled, rounded"];
  d1 [href="/holiday-project-day-01/", label="D1\nkarma&jasmine"];
  d2 [href="/holiday-project-day-02/", label="D2\njasmine-node"];
  d3 [href="/holiday-project-day-03/", label="D3\nsupertest"];
  d4 [href="/holiday-project-day-04/", label="D4\nbower"];
  d5 [href="/holiday-project-day-05/", label="D5\nhighlight.js"];
  d6 [href="/holiday-project-day-06/", label="D6\ngraghviz\nD3.js"];
  d7 [href="/holiday-project-day-07/", label="D7\ngraphviz\n웹링크"];

  시작 -> d1 -> d2 -> d3;
  시작 -> d4 -> d5;
  d1 -> d5;
  d4 -> d6 -> d7;
}
```

역시 ```dot 명령어```로 SVG 파일을 만들고,

```bash
dot -Tsvg < day-07.dot > day-07.svg
```

결과 SVG의 모습은 아래와 같다.

### day-07.svg

[graphviz]: http://www.graphviz.org/
[dot]: http://www.graphviz.org/content/dot-language
[D3.js]: http://d3js.org/
[어제]: /holiday-project-day-06/

<svg width="434pt" height="167pt"
 viewBox="0.00 0.00 434.00 167.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
<g id="graph0" class="graph" transform="scale(1 1) rotate(0) translate(4 163)">
<title>G</title>
<polygon fill="white" stroke="none" points="-4,4 -4,-163 430,-163 430,4 -4,4"/>
<!-- 시작 -->
<g id="node1" class="node"><title>시작</title>
<path fill="deepskyblue" stroke="black" d="M42,-103C42,-103 12,-103 12,-103 6,-103 0,-97 0,-91 0,-91 0,-79 0,-79 0,-73 6,-67 12,-67 12,-67 42,-67 42,-67 48,-67 54,-73 54,-79 54,-79 54,-91 54,-91 54,-97 48,-103 42,-103"/>
<text text-anchor="middle" x="27" y="-83.1437" font-family="Times,serif" font-size="9.00" fill="white">시작</text>
</g>
<!-- d1 -->
<g id="node2" class="node"><title>d1</title>
<g id="a_node2"><a xlink:href="/holiday-project-day-01/" xlink:title="D1\nkarma&amp;jasmine">
<ellipse fill="steelblue" stroke="black" cx="142" cy="-113" rx="51.9171" ry="18.2703"/>
<text text-anchor="middle" x="142" y="-114.8" font-family="Times,serif" font-size="9.00" fill="white">D1</text>
<text text-anchor="middle" x="142" y="-105.8" font-family="Times,serif" font-size="9.00" fill="white">karma&amp;jasmine</text>
</a>
</g>
</g>
<!-- 시작&#45;&gt;d1 -->
<g id="edge1" class="edge"><title>시작&#45;&gt;d1</title>
<path fill="none" stroke="black" d="M54.1786,-97.8079C62.6589,-100.938 71.6979,-103.065 86.2651,-105.398"/>
<polygon fill="black" stroke="black" points="86.0741,-107.848 93.365,-106.489 86.8184,-103.005 86.0741,-107.848"/>
</g>
<!-- d4 -->
<g id="node5" class="node"><title>d4</title>
<g id="a_node5"><a xlink:href="/holiday-project-day-04/" xlink:title="D4\nbower">
<ellipse fill="steelblue" stroke="black" cx="142" cy="-58" rx="27.4365" ry="18.2703"/>
<text text-anchor="middle" x="142" y="-59.8" font-family="Times,serif" font-size="9.00" fill="white">D4</text>
<text text-anchor="middle" x="142" y="-50.8" font-family="Times,serif" font-size="9.00" fill="white">bower</text>
</a>
</g>
</g>
<!-- 시작&#45;&gt;d4 -->
<g id="edge4" class="edge"><title>시작&#45;&gt;d4</title>
<path fill="none" stroke="black" d="M54.061,-73.959C66.1032,-69.9838 79.3213,-67.48 108.048,-63.0827"/>
<polygon fill="black" stroke="black" points="108.71,-65.4603 115.263,-61.9869 107.974,-60.6158 108.71,-65.4603"/>
</g>
<!-- d2 -->
<g id="node3" class="node"><title>d2</title>
<g id="a_node3"><a xlink:href="/holiday-project-day-02/" xlink:title="D2\njasmine&#45;node">
<ellipse fill="steelblue" stroke="black" cx="276" cy="-141" rx="45.6956" ry="18.2703"/>
<text text-anchor="middle" x="276" y="-142.8" font-family="Times,serif" font-size="9.00" fill="white">D2</text>
<text text-anchor="middle" x="276" y="-133.8" font-family="Times,serif" font-size="9.00" fill="white">jasmine&#45;node</text>
</a>
</g>
</g>
<!-- d1&#45;&gt;d2 -->
<g id="edge2" class="edge"><title>d1&#45;&gt;d2</title>
<path fill="none" stroke="black" d="M167.258,-128.92C192.989,-144.644 206.062,-149.835 226.772,-148.733"/>
<polygon fill="black" stroke="black" points="227.013,-151.171 233.791,-148.159 226.614,-146.287 227.013,-151.171"/>
</g>
<!-- d5 -->
<g id="node6" class="node"><title>d5</title>
<g id="a_node6"><a xlink:href="/holiday-project-day-05/" xlink:title="D5\nhighlight.js">
<ellipse fill="steelblue" stroke="black" cx="276" cy="-86" rx="40.264" ry="18.2703"/>
<text text-anchor="middle" x="276" y="-87.8" font-family="Times,serif" font-size="9.00" fill="white">D5</text>
<text text-anchor="middle" x="276" y="-78.8" font-family="Times,serif" font-size="9.00" fill="white">highlight.js</text>
</a>
</g>
</g>
<!-- d1&#45;&gt;d5 -->
<g id="edge6" class="edge"><title>d1&#45;&gt;d5</title>
<path fill="none" stroke="black" d="M189.157,-120.909C205.994,-121.374 219.246,-116.64 243.707,-103.726"/>
<polygon fill="black" stroke="black" points="245.161,-105.727 250.177,-100.265 242.849,-101.407 245.161,-105.727"/>
</g>
<!-- d3 -->
<g id="node4" class="node"><title>d3</title>
<g id="a_node4"><a xlink:href="/holiday-project-day-03/" xlink:title="D3\nsupertest">
<ellipse fill="steelblue" stroke="black" cx="392" cy="-141" rx="33.8713" ry="18.2703"/>
<text text-anchor="middle" x="392" y="-142.8" font-family="Times,serif" font-size="9.00" fill="white">D3</text>
<text text-anchor="middle" x="392" y="-133.8" font-family="Times,serif" font-size="9.00" fill="white">supertest</text>
</a>
</g>
</g>
<!-- d2&#45;&gt;d3 -->
<g id="edge3" class="edge"><title>d2&#45;&gt;d3</title>
<path fill="none" stroke="black" d="M319.567,-146.626C334.978,-148.418 345.139,-149.143 353.802,-148.802"/>
<polygon fill="black" stroke="black" points="354.017,-151.243 360.809,-148.264 353.642,-146.357 354.017,-151.243"/>
</g>
<!-- d4&#45;&gt;d5 -->
<g id="edge5" class="edge"><title>d4&#45;&gt;d5</title>
<path fill="none" stroke="black" d="M168.514,-53.4193C192.734,-50.1624 205.654,-53.2241 241.73,-69.8035"/>
<polygon fill="black" stroke="black" points="240.849,-72.0956 248.23,-72.8181 242.91,-67.6503 240.849,-72.0956"/>
</g>
<!-- d6 -->
<g id="node7" class="node"><title>d6</title>
<g id="a_node7"><a xlink:href="/holiday-project-day-06/" xlink:title="D6\ngraghviz\nD3.js">
<ellipse fill="steelblue" stroke="black" cx="276" cy="-25" rx="33.6567" ry="25"/>
<text text-anchor="middle" x="276" y="-31.3" font-family="Times,serif" font-size="9.00" fill="white">D6</text>
<text text-anchor="middle" x="276" y="-22.3" font-family="Times,serif" font-size="9.00" fill="white">graghviz</text>
<text text-anchor="middle" x="276" y="-13.3" font-family="Times,serif" font-size="9.00" fill="white">D3.js</text>
</a>
</g>
</g>
<!-- d4&#45;&gt;d6 -->
<g id="edge7" class="edge"><title>d4&#45;&gt;d6</title>
<path fill="none" stroke="black" d="M161.213,-44.9103C194.561,-22.5222 207.28,-17.0233 235.758,-19.4524"/>
<polygon fill="black" stroke="black" points="235.717,-21.9108 242.93,-20.1848 236.215,-17.0362 235.717,-21.9108"/>
</g>
<!-- d7 -->
<g id="node8" class="node"><title>d7</title>
<g id="a_node8"><a xlink:href="/holiday-project-day-07/" xlink:title="D7\ngraphviz\n웹링크">
<ellipse fill="steelblue" stroke="black" cx="392" cy="-25" rx="33.6567" ry="25.8841"/>
<text text-anchor="middle" x="392" y="-32.1437" font-family="Times,serif" font-size="9.00" fill="white">D7</text>
<text text-anchor="middle" x="392" y="-23.1438" font-family="Times,serif" font-size="9.00" fill="white">graphviz</text>
<text text-anchor="middle" x="392" y="-14.1438" font-family="Times,serif" font-size="9.00" fill="white">웹링크</text>
</a>
</g>
</g>
<!-- d6&#45;&gt;d7 -->
<g id="edge8" class="edge"><title>d6&#45;&gt;d7</title>
<path fill="none" stroke="black" d="M309.408,-21.0425C329.839,-18.7235 342.317,-17.6695 352.454,-17.8806"/>
<polygon fill="black" stroke="black" points="352.382,-20.3301 359.503,-18.2579 352.644,-15.4372 352.382,-20.3301"/>
</g>
</g>
</svg>

d[1-7] 노드에 마우스 포인터를 올려다 놓으면 알 수 있듯이, 웹 링크가 잘 걸려있다. 이런 방식으로 그래프 node, edge, label에 모두 ```href```속성으로 아무런 문제 없이 링크를 걸 수 있으며, 결과 포맷이 svg 나 map일 때 해당 링크가 적용돼 출력된다.

D3.js가 훨씬 화려하기는 하지만, graphviz 역시 나름의 강점을 바탕으로 활용해볼 만한 여지가 크다. 게다가 graphviz로는 SVG뿐 아니라, png나 pdf 파일로도 만들기 편리하다는 점도 기억해 둘만 하다.

오늘은 여기까지.
