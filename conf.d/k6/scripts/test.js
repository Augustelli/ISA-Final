import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

let logCounter = new Counter('generated_logs');

export let options = {
  stages: [
    { duration: '30s', target: 10 }, // Simula 10 usuarios durante 30 segundos
    { duration: '1m', target: 20 },  // Incrementa a 20 usuarios durante 1 minuto
    { duration: '30s', target: 0 },  // Reduce a 0 usuarios durante 30 segundos
  ],
};

export default function () {
  let loginRes = http.post('http://isa-final-isabe-1:8080/api/authenticate', {
    username: 'admin',
    password: 'admin',
    rememberMe: false
  });
  check(loginRes, {
    'login status was 200': (r) => r.status == 200,
  });
  let idToken = loginRes.json('id_token');
  console.log(`Generated log entry for login at ${new Date().toISOString()}`);
  logCounter.add(1);

  let headers = {
    headers: {
      Authorization: `Bearer ${idToken}`,
    },
  };

  let getContadorRes = http.get('http://isa-final-isabe-1:8080/api/getContador', headers);
  check(getContadorRes, {
    'getContador status was 200': (r) => r.status == 200,
  });
  console.log(`Generated log entry for getContador at ${new Date().toISOString()}`);
  logCounter.add(1);

  let contadorPostRes = http.post('http://isa-final-isabe-1:8080/api/contadorPost', { value: 1 }, headers);
  check(contadorPostRes, {
    'contadorPost status was 200': (r) => r.status == 200,
  });
  console.log(`Generated log entry for contadorPost at ${new Date().toISOString()}`);
  logCounter.add(1);

  sleep(1);
}