const students = [];

function parseBody(req) {
  if (!req.body) {
    return {};
  }

  if (typeof req.body === "string") {
    try {
      return JSON.parse(req.body);
    } catch {
      return {};
    }
  }

  return req.body;
}

function sendJson(res, status, payload) {
  res.status(status).json(payload);
}

function validateStudent(input) {
  const rollNo = Number(input.rollNo);
  const name = String(input.name || "").trim();
  const branch = String(input.branch || "").trim();

  if (!Number.isInteger(rollNo) || rollNo <= 0) {
    return { error: "rollNo must be a positive integer" };
  }

  if (!name) {
    return { error: "name is required" };
  }

  if (!branch) {
    return { error: "branch is required" };
  }

  return { student: { rollNo, name, branch } };
}

export default function handler(req, res) {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type");

  if (req.method === "OPTIONS") {
    return res.status(200).end();
  }

  if (req.method === "GET") {
    return sendJson(res, 200, { students });
  }

  if (req.method === "POST") {
    const body = parseBody(req);
    const { student, error } = validateStudent(body);

    if (error) {
      return sendJson(res, 400, { error });
    }

    const exists = students.some((item) => item.rollNo === student.rollNo);
    if (exists) {
      return sendJson(res, 409, { error: "Student with this roll number already exists" });
    }

    students.push(student);
    return sendJson(res, 201, { student, students });
  }

  if (req.method === "PUT") {
    const body = parseBody(req);
    const { student, error } = validateStudent(body);

    if (error) {
      return sendJson(res, 400, { error });
    }

    const index = students.findIndex((item) => item.rollNo === student.rollNo);
    if (index === -1) {
      return sendJson(res, 404, { error: "Student not found" });
    }

    students[index] = student;
    return sendJson(res, 200, { student, students });
  }

  if (req.method === "DELETE") {
    const rollNo = Number(req.query.rollNo);
    if (!Number.isInteger(rollNo) || rollNo <= 0) {
      return sendJson(res, 400, { error: "Valid rollNo query parameter is required" });
    }

    const index = students.findIndex((item) => item.rollNo === rollNo);
    if (index === -1) {
      return sendJson(res, 404, { error: "Student not found" });
    }

    const [deleted] = students.splice(index, 1);
    return sendJson(res, 200, { deleted, students });
  }

  return sendJson(res, 405, { error: "Method not allowed" });
}
