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
  // Reemplaza 'http://jhipster-app:8080/api/endpoint' con el endpoint real de tu aplicación JHipster
  let res = http.get('http://isabe:8080/api/endpoint');
  check(res, {
    'status was 200': (r) => r.status == 200,
  });

  // Generar un log de prueba
  console.log(`Generated log entry at ${new Date().toISOString()}`);
  logCounter.add(1);

  sleep(1);
}