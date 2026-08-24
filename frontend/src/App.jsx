import { useEffect, useMemo, useState } from 'react';
import './index.css';

const API = "/api/v1";

// --- ICONS (SVG) ---
const Icons = {
  Users: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>,
  Calendar: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>,
  Briefcase: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path></svg>,
  Bell: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path></svg>,
  Plus: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4"></path></svg>,
  Check: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7"></path></svg>,
  X: ({ size }) => <svg width={size || 24} height={size || 24} fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12"></path></svg>,
  Heart: ({ solid }) => <svg fill={solid ? "currentColor" : "none"} stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path></svg>,
  ThumbsDown: ({ solid }) => <svg fill={solid ? "currentColor" : "none"} stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M10 14H5.236a2 2 0 01-1.789-2.894l3.5-7A2 2 0 018.736 3h4.018a2 2 0 01.485.06l3.76.94m-7 10v5a2 2 0 002 2h.096c.5 0 .905-.405.905-.904 0-.714.211-1.412.608-2.006L17 13V4m-7 10h2m5-10h2a2 2 0 012 2v6a2 2 0 01-2 2h-2.5"></path></svg>,
  Chat: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>,
  User: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>,
  Eye: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path><path strokeLinecap="round" strokeLinejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path></svg>,
  EyeOff: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.542-7a9.978 9.978 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.542 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"></path></svg>,
  Logout: () => <svg fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>,
  Camera: ({ size }) => <svg width={size || 24} height={size || 24} fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M3 7a2 2 0 012-2h4l1.5-2h3L15 5h4a2 2 0 012 2v10a2 2 0 01-2 2H5a2 2 0 01-2-2V7z"></path><path strokeLinecap="round" strokeLinejoin="round" d="M12 15a4 4 0 100-8 4 4 0 000 8z"></path></svg>,
  Lock: ({ size }) => <svg width={size || 24} height={size || 24} fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path></svg>
};

// --- SHARED COMPONENTS ---
function Modal({ title, isOpen, onClose, children }) {
  if (!isOpen) return null;
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{title}</h2>
          <button className="modal-close" onClick={onClose}><Icons.X /></button>
        </div>
        <div className="modal-body">
          {children}
        </div>
      </div>
    </div>
  );
}

function StatusBadge({ value }) {
  let cls = "";
  if (["ONAYLANDI", "APPROVED"].includes(value)) cls = "success";
  else if (["REDDEDILDI", "ARIZALI", "REJECTED", "FAULTY"].includes(value)) cls = "danger";
  else if (["BEKLIYOR", "ONAY_BEKLIYOR", "PENDING"].includes(value)) cls = "warn";
  else if (["DEPODA", "İADE EDİLDİ", "IADE EDİLDİ", "IN_STORAGE", "RETURNED"].includes(value)) cls = "info";
  else if (["KULLANIMDA", "IN_USE", "PERSONELDE", "ASSIGNED"].includes(value)) cls = "orange";

  const statusMap = {
    "ONAYLANDI": "APPROVED",
    "REDDEDILDI": "REJECTED",
    "BEKLIYOR": "PENDING",
    "ONAY_BEKLIYOR": "PENDING APPROVAL",
    "DEPODA": "IN STORAGE",
    "İADE EDİLDİ": "RETURNED",
    "IADE EDİLDİ": "RETURNED",
    "KULLANIMDA": "IN USE",
    "PERSONELDE": "ASSIGNED",
    "ARIZALI": "FAULTY",
    "YILLIK_IZIN": "ANNUAL LEAVE",
    "MAZERET_IZNI": "CASUAL LEAVE",
    "HASTALIK_IZNI": "SICK LEAVE"
  };

  const label = statusMap[value] || value;
  return <span className={`badge ${cls}`}>{label}</span>;
}

// Date Logic Helper
function getWorkingDays(s, e) {
  if (!s || !e) return 0;
  const start = new Date(s); const end = new Date(e);
  let count = 0;
  const cur = new Date(start);
  while (cur <= end) {
    const day = cur.getDay();
    if (day !== 0 && day !== 6) count++;
    cur.setDate(cur.getDate() + 1);
  }
  return count;
}

// --- MAIN APP ---
function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [currentUserEmail, setCurrentUserEmail] = useState(localStorage.getItem("email") || "");
  const [tab, setTab] = useState("dashboard");
  const [data, setData] = useState({ personel: [], leaves: [], rooms: [], reservations: [], equipmentTypes: [], equipments: [], announcements: [], departments: [], roles: [] });
  const [mockEqRequests, setMockEqRequests] = useState([]); // Mock UI state for equipment requests

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const authHeaders = token ? { Authorization: `Bearer ${token}` } : {};

  async function request(path, options = {}) {
    const response = await fetch(`${API}${path}`, {
      ...options,
      headers: { "Content-Type": "application/json", ...authHeaders, ...(options.headers || {}) }
    });
    if (response.status === 401 || response.status === 403) {
      localStorage.clear();
      setToken("");
      throw new Error("Session expired.");
    }
    if (response.status === 204) return null;
    const text = await response.text();
    const body = text ? JSON.parse(text) : null;
    if (!response.ok) throw new Error(body?.message || body?.error || "Operation failed (HTTP " + response.status + ")");
    return body;
  }

  async function login(email, password) {
    setError("");
    const response = await fetch(`${API}/auth/login`, {
      method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ email, password })
    });
    const body = await response.json();
    if (!response.ok) throw new Error(body?.message || "Login failed");
    localStorage.setItem("token", body.accessToken);
    localStorage.setItem("email", email);
    setToken(body.accessToken);
    setCurrentUserEmail(email);
  }

  async function loadAll() {
    if (!token) return;
    setLoading(true);
    setError("");
    setMessage("");
    try {
      const [personel, leaves, rooms, reservations, equipmentTypes, equipments, announcements, departments, roles, equipmentAssignments] = await Promise.all([
        request("/personel/get/all").catch(() => []),
        request("/leave-request/get/all").catch(() => []),
        request("/meeting-rooms/get/all").catch(() => []),
        request("/room-reservation/get/all").catch(() => []),
        request("/equipment-types/get/all").catch(() => []),
        request("/equipments/get/all").catch(() => []),
        request("/announcement/get/all").catch(() => []),
        request("/department/get/all").catch(() => []),
        request("/role/get/all").catch(() => []),
        request("/equipment-assignments/get/all").catch(() => [])
      ]);
      setData({ personel, leaves, rooms, reservations, equipmentTypes, equipments, announcements, departments, roles });
      setMockEqRequests(equipmentAssignments || []);
    } catch (err) {
      setError("Error loading data: " + err.message);
    } finally { setLoading(false); }
  }

  useEffect(() => { loadAll(); }, [token]);

  function done(text) {
    setMessage(text); setError("");
    setTimeout(() => setMessage(""), 3000);
    loadAll();
  }

  function logout() {
    localStorage.clear();
    setToken("");
  }

  const currentPersonel = data.personel.find(p => p.email === currentUserEmail) || {};

  // RBAC Variables
  const userRole = (currentPersonel.roleName || "EMPLOYEE").toUpperCase();
  const isAdmin = userRole === "ADMIN" || userRole.includes("ADMIN");
  const isManager = userRole === "MANAGER" || userRole.includes("MANAGER") || userRole.includes("YÖNET") || userRole.includes("YONET") || userRole.includes("MÜDÜR") || userRole.includes("MUDUR");
  const isPersonel = !isAdmin && !isManager;
  const roleDisplay = isAdmin ? "ADMIN" : isManager ? "MANAGER" : "EMPLOYEE";

  const navTabs = [
    ["dashboard", "Dashboard", Icons.Briefcase],
    ["directory", "Team Directory", Icons.Users],
    ["leaves", "Leave Requests", Icons.Calendar],
    ["rooms", "Meeting Rooms", Icons.Users],
    ["equipment", "Equipment", Icons.Briefcase],
    ["announcements", "Announcements", Icons.Bell]
  ];
  if (isAdmin || isManager) {
    navTabs.push(["approvals", "Manager Approvals", Icons.Check]);
  }
  navTabs.push(["profile", "My Profile", Icons.User]);

  if (!token) return <Login onLogin={login} error={error} setError={setError} />;

  return (
    <div className="shell">
      <aside className="sidebar">
        <div className="brand">
          <strong>Enterprise Portal</strong>
        </div>
        <nav className="nav">
          {navTabs.map(([key, label, Icon]) => (
            <button key={key} className={tab === key ? "active" : ""} onClick={() => setTab(key)}>
              <Icon /> {label}
            </button>
          ))}
          <button className="logout-btn" onClick={logout}><Icons.Logout /> Sign Out</button>
        </nav>
      </aside>
      <main className="main">
        <div className="topbar">
          <div>
            <h1>{navTabs.find(([key]) => key === tab)?.[1]}</h1>
          </div>
          <div className="userbar">
            <span className="role-badge">{roleDisplay}</span>
            <span>{currentPersonel.firstName} {currentPersonel.lastName}</span>
          </div>
        </div>
        {message && <div className="message"><Icons.Check /> {message}</div>}
        {error && <div className="message error"><Icons.X /> {error}</div>}

        {tab === "dashboard" && <Dashboard data={data} setTab={setTab} currentPersonel={currentPersonel} mockEqRequests={mockEqRequests} />}
        {tab === "directory" && <Directory data={data} />}
        {tab === "leaves" && <Leaves data={data} request={request} done={done} currentPersonel={currentPersonel} isAdmin={isAdmin} isManager={isManager} />}
        {tab === "rooms" && <Rooms data={data} request={request} done={done} currentPersonel={currentPersonel} />}
        {tab === "equipment" && <Equipment data={data} request={request} done={done} currentPersonel={currentPersonel} isAdmin={isAdmin} isManager={isManager} mockEqRequests={mockEqRequests} setMockEqRequests={setMockEqRequests} />}
        {tab === "announcements" && <Announcements data={data} request={request} done={done} currentPersonel={currentPersonel} isAdmin={isAdmin} />}
        {tab === "profile" && <Profile data={data} currentPersonel={currentPersonel} request={request} done={done} />}
        {tab === "approvals" && (isAdmin || isManager) && <Approvals data={data} request={request} done={done} mockEqRequests={mockEqRequests} setMockEqRequests={setMockEqRequests} currentPersonel={currentPersonel} />}
      </main>
    </div>
  );
}

// --- MODULE 1: DASHBOARD (ALL ROLES) ---
function Dashboard({ data, setTab, currentPersonel, mockEqRequests }) {
  const [appreciations, setAppreciations] = useState([
    { id: 1, text: "Thanks to Aslan for the great support!", author: "Melisa" }
  ]);
  const [newAppreciation, setNewAppreciation] = useState("");

  function postAppreciation(e) {
    e.preventDefault();
    if (!newAppreciation.trim()) return;
    setAppreciations([{ id: Date.now(), text: newAppreciation, author: currentPersonel.firstName }, ...appreciations]);
    setNewAppreciation("");
  }

  const today = new Date();
  const bdays = data.personel.filter(p => {
    if (!p.birthOfDate) return false;
    const d = new Date(p.birthOfDate);
    return d.getMonth() === today.getMonth() && d.getDate() === today.getDate();
  });

  const TOTAL_LEAVES = 15; // maximum annual leave allowance
  const myLeaves = data.leaves.filter(l => l.personelId === currentPersonel.id);
  const myApprovedLeaveDays = myLeaves
    .filter(l => l.leaveStatus === "ONAYLANDI")
    .reduce((sum, l) => {
      const start = new Date(l.startDate);
      const end = new Date(l.endDate);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      return sum + diffDays;
    }, 0);
  const remainingLeaves = Math.max(0, TOTAL_LEAVES - myApprovedLeaveDays);

  const peopleOnLeaveToday = data.leaves.filter(l => {
    if (l.leaveStatus !== "ONAYLANDI") return false;
    const start = new Date(l.startDate);
    const end = new Date(l.endDate);
    return today >= start && today <= end;
  }).length;

  const getInitials = (name) => {
    if (!name) return "U";
    return name.split(" ").map(n => n[0]).join("").toUpperCase().substring(0, 2);
  };

  const getRoomStatus = (roomId) => {
    const activeRes = data.reservations.find(r => {
      if (r.roomId !== roomId) return false;
      const start = new Date(r.startTime);
      const end = new Date(r.endTime);
      return today >= start && today <= end;
    });
    if (activeRes) {
      const endTimeStr = new Date(activeRes.endTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
      return { text: `Occupied until ${endTimeStr}`, color: 'var(--danger)', bg: '#fee2e2' };
    }
    return { text: "Available", color: 'var(--success)', bg: '#dcfce7' };
  };

  const buyukSalonStatus = getRoomStatus(1);
  const kucukOdaStatus = getRoomStatus(2);

  const myEqReqs = (mockEqRequests || []).filter(r => r.personelId === currentPersonel.id);
  const latestEqReq = myEqReqs.length > 0 ? myEqReqs[myEqReqs.length - 1] : null;

  return (
    <div className="grid">
      {/* Meeting Rooms Summary Card */}
      <div className="panel stat-card quarter" onClick={() => setTab("rooms")} style={{ cursor: 'pointer' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', width: '100%', justifyContent: 'center' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontWeight: '500', fontSize: '13px' }}>Main Hall</span>
            <span className="badge" style={{ color: buyukSalonStatus.color, backgroundColor: buyukSalonStatus.bg }}>{buyukSalonStatus.text}</span>
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontWeight: '500', fontSize: '13px' }}>Small Room</span>
            <span className="badge" style={{ color: kucukOdaStatus.color, backgroundColor: kucukOdaStatus.bg }}>{kucukOdaStatus.text}</span>
          </div>
        </div>
      </div>

      {/* Equipment Request Tracker Card */}
      <div className="panel stat-card quarter" onClick={() => setTab("equipment")} style={{ cursor: 'pointer' }}>
        <div className="stat-icon" style={{ color: '#ea580c', background: '#ffedd5' }}><Icons.Briefcase /></div>
        <div className="stat-content" style={{ flex: 1 }}>
          <span style={{ fontSize: '13px' }}>Latest Equipment Request</span>
          {latestEqReq ? (
            <div style={{ marginTop: '4px' }}>
              <b style={{ display: 'block', fontSize: '14px', marginBottom: '4px' }}>{latestEqReq.typeName}</b>
              <StatusBadge value={latestEqReq.status} />
            </div>
          ) : (
            <span style={{ color: 'var(--muted)', fontSize: '12px' }}>No requests</span>
          )}
        </div>
      </div>

      {/* Birthdays Card */}
      <div className="panel stat-card half">
        <div className="stat-icon" style={{ color: '#e11d48', background: '#ffe4e6' }}><Icons.Heart /></div>
        <div className="stat-content" style={{ flex: 1 }}>
          <span>Today's Birthdays</span>
          {bdays.length > 0 ? (
            <div style={{ display: 'flex', gap: '8px', marginTop: '8px', flexWrap: 'wrap' }}>
              {bdays.map(p => (
                <div key={p.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', background: '#f8fafc', padding: '4px 8px', borderRadius: '4px' }}>
                  <div style={{ width: '28px', height: '28px', borderRadius: '50%', background: 'var(--brand)', color: 'white', display: 'grid', placeItems: 'center', fontSize: '11px', fontWeight: 'bold' }}>
                    {getInitials(`${p.firstName} ${p.lastName}`)}
                  </div>
                  <div style={{ fontSize: '13px' }}>
                    <b style={{ display: 'block' }}>{p.firstName} {p.lastName}</b>
                    <span style={{ color: 'var(--muted)', display: 'block', fontSize: '11px' }}>{p.departmentName || "No Department"}</span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <span style={{ color: 'var(--muted)', fontSize: '13px', marginTop: '4px', display: 'block' }}>No birthdays today.</span>
          )}
        </div>
      </div>

      {/* 5. Ekip Dizini Preview */}
      <div className="panel half" onClick={() => setTab("directory")} style={{ cursor: 'pointer' }}>
        <h3 style={{ marginBottom: '12px', fontSize: '15px' }}><Icons.Users /> Team Directory</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
          {data.personel.slice(0, 2).map(p => (
            <div key={p.id} style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '8px', border: '1px solid var(--line)', borderRadius: '6px' }}>
              <div style={{ width: '32px', height: '32px', borderRadius: '50%', background: '#e2e8f0', color: '#475569', display: 'grid', placeItems: 'center', fontSize: '12px', fontWeight: 'bold' }}>
                {getInitials(`${p.firstName} ${p.lastName}`)}
              </div>
              <div>
                <b style={{ fontSize: '14px', display: 'block' }}>{p.firstName} {p.lastName}</b>
                <span style={{ fontSize: '12px', color: 'var(--muted)' }}>{p.departmentName} &middot; {(p.skills || []).slice(0, 2).join(", ")}</span>
              </div>
            </div>
          ))}
          <div style={{ fontSize: '12px', color: 'var(--brand)', textAlign: 'center', marginTop: '4px' }}>View All &rarr;</div>
        </div>
      </div>

      {/* Leave Status Card */}
      <div className="panel half" style={{ display: 'flex', flexDirection: 'column', cursor: 'pointer' }} onClick={() => setTab("leaves")}>
        <h3 style={{ marginBottom: '12px', fontSize: '15px' }}><Icons.Calendar /> Leave Status</h3>
        <div style={{ display: 'flex', gap: '16px', flex: 1 }}>
          <div style={{ flex: 1, background: '#f8fafc', padding: '16px', borderRadius: '8px', textAlign: 'center' }}>
            <span style={{ display: 'block', color: 'var(--muted)', fontSize: '13px', marginBottom: '8px' }}>My Remaining Leave</span>
            <b style={{ fontSize: '24px', color: 'var(--brand)' }}>{remainingLeaves} days</b>
          </div>
          <div style={{ flex: 1, background: '#f8fafc', padding: '16px', borderRadius: '8px', textAlign: 'center' }}>
            <span style={{ display: 'block', color: 'var(--muted)', fontSize: '13px', marginBottom: '8px' }}>On Leave Today</span>
            <b style={{ fontSize: '24px', color: '#0f172a' }}>{peopleOnLeaveToday} people</b>
          </div>
        </div>
      </div>

      {/* Appreciation Board */}
      <div className="panel" style={{ gridColumn: '1 / -1' }}>
        <h3 style={{ marginBottom: '16px', fontSize: '15px' }}>Appreciation Board</h3>
        <div style={{ display: 'flex', gap: '24px', alignItems: 'center' }}>
          <div style={{ flex: 1, fontStyle: 'italic', fontSize: '18px', color: '#475569', borderLeft: '4px solid var(--brand)', paddingLeft: '16px' }}>
            "{appreciations[0].text}"
            <div style={{ fontSize: '14px', fontWeight: 'bold', fontStyle: 'normal', marginTop: '8px', color: '#0f172a' }}>— {appreciations[0].author}</div>
          </div>
          <form onSubmit={postAppreciation} style={{ flex: 1, display: 'flex', gap: '8px' }}>
            <input value={newAppreciation} onChange={e => setNewAppreciation(e.target.value)} placeholder="Say thanks to someone..." style={{ flex: 1 }} />
            <button type="submit">Submit</button>
          </form>
        </div>
      </div>
    </div>
  );
}

// --- MODULE 2: DIRECTORY ---
function Directory({ data }) {
  const [query, setQuery] = useState("");
  const [deptFilter, setDeptFilter] = useState("");
  const [roleFilter, setRoleFilter] = useState("");

  const filtered = data.personel.filter(p => {
    const matchQ = `${p.firstName} ${p.lastName} ${(p.skills || []).join(" ")}`.toLowerCase().includes(query.toLowerCase());
    const matchD = deptFilter ? p.departmentName === deptFilter : true;
    const matchR = roleFilter ? p.roleName === roleFilter : true;
    return matchQ && matchD && matchR;
  });

  const uniqueDepts = [...new Set(data.personel.map(p => p.departmentName).filter(Boolean))];
  const uniqueRoles = [...new Set(data.personel.map(p => p.roleName).filter(Boolean))];

  function isManagerRole(roleName) {
    if (!roleName) return false;
    const upper = roleName.toUpperCase();
    return upper.includes("ADMIN") || upper.includes("MANAGER") || upper.includes("YÖNET") || upper.includes("YONET") || upper.includes("MÜDÜR") || upper.includes("MUDUR");
  }

  const getInitials = (name) => {
    if (!name) return "U";
    return name.split(" ").map(n => n[0]).join("").toUpperCase().substring(0, 2);
  };

  const groupedByDept = {};
  filtered.forEach(p => {
    const dept = p.departmentName || "Independent / Other";
    if (!groupedByDept[dept]) groupedByDept[dept] = { managers: [], personnel: [] };
    if (isManagerRole(p.roleName)) {
      groupedByDept[dept].managers.push(p);
    } else {
      groupedByDept[dept].personnel.push(p);
    }
  });

  const renderPersonCard = (p, isManager) => (
    <div key={p.id} style={{ display: 'flex', gap: '16px', alignItems: 'center', padding: '12px 16px', background: isManager ? '#f8fafc' : 'white', border: isManager ? '1px solid #e2e8f0' : '1px solid var(--line)', borderRadius: '8px', borderLeft: isManager ? '4px solid var(--brand)' : '1px solid var(--line)' }}>
      <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: isManager ? 'var(--brand)' : '#e2e8f0', color: isManager ? 'white' : '#475569', display: 'grid', placeItems: 'center', fontSize: '14px', fontWeight: 'bold' }}>
        {getInitials(`${p.firstName} ${p.lastName}`)}
      </div>
      <div style={{ flex: 1 }}>
        <b style={{ display: 'block', fontSize: '15px' }}>{p.firstName} {p.lastName}</b>
        <span style={{ fontSize: '12px', color: 'var(--muted)', display: 'block' }}>{p.email}</span>
      </div>
      <div style={{ textAlign: 'right' }}>
        <span className="role-badge" style={{ fontSize: '11px', display: 'inline-block', marginBottom: '4px' }}>{p.roleName || '-'}</span>
        <div style={{ fontSize: '11px', color: 'var(--muted)' }}>
          {(p.skills || []).length > 0 ? (p.skills || []).slice(0,2).join(", ") + ((p.skills || []).length > 2 ? '...' : '') : 'No skills listed'}
        </div>
      </div>
    </div>
  );

  return (
    <div className="panel" style={{ display: 'grid', gap: '24px' }}>
      <div style={{ display: 'flex', gap: '16px', alignItems: 'flex-end', flexWrap: 'wrap' }}>
        <label style={{ flex: 1, minWidth: '200px' }}>Search by Name or Skill
          <input value={query} onChange={e => setQuery(e.target.value)} placeholder="e.g. John, Java..." />
        </label>
        <label style={{ width: '200px' }}>Department
          <select value={deptFilter} onChange={e => setDeptFilter(e.target.value)}>
            <option value="">All</option>
            {uniqueDepts.map(d => <option key={d} value={d}>{d}</option>)}
          </select>
        </label>
        <label style={{ width: '200px' }}>Role
          <select value={roleFilter} onChange={e => setRoleFilter(e.target.value)}>
            <option value="">All</option>
            {uniqueRoles.map(r => <option key={r} value={r}>{r}</option>)}
          </select>
        </label>
      </div>

      <div className="tree-container" style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
        {Object.keys(groupedByDept).length === 0 && (
          <div style={{ textAlign: 'center', color: 'var(--muted)', padding: '32px' }}>No records found.</div>
        )}
        
        {Object.entries(groupedByDept).map(([deptName, group]) => (
          <div key={deptName} className="dept-tree" style={{ border: '1px solid var(--line)', borderRadius: '12px', padding: '24px', background: 'white', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '20px' }}>
              <div style={{ background: '#e0f2fe', color: '#0369a1', padding: '8px', borderRadius: '8px' }}>
                <Icons.Briefcase />
              </div>
              <h3 style={{ fontSize: '18px', margin: 0 }}>{deptName}</h3>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {group.managers.length > 0 ? (
                group.managers.map(m => renderPersonCard(m, true))
              ) : (
                <div style={{ fontSize: '13px', color: 'var(--muted)', fontStyle: 'italic', paddingLeft: '16px' }}>No manager assigned to this department.</div>
              )}

              {group.personnel.length > 0 && (
                <div style={{ paddingLeft: '40px', paddingTop: '28px', borderLeft: '2px solid #e2e8f0', marginLeft: '20px', display: 'flex', flexDirection: 'column', gap: '12px', position: 'relative' }}>
                  <div style={{ position: 'absolute', top: '-12px', left: '-12px', background: 'white', padding: '4px', fontSize: '12px', color: '#94a3b8', fontWeight: 'bold' }}>
                    <Icons.Users /> Team Members
                  </div>
                  {group.personnel.map(p => renderPersonCard(p, false))}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

// --- MODULE 3: LEAVES (Personel/Manager Roles) ---
function Leaves({ data, request, done, currentPersonel, isAdmin, isManager }) {
  const [isOpen, setIsOpen] = useState(false);
  const today = new Date().toISOString().split('T')[0];
  const [form, setForm] = useState({ leaveType: "YILLIK_IZIN", startDate: today, endDate: today, managerNote: "" });
  const showTabs = isAdmin || isManager;
  const [activeTab, setActiveTab] = useState("my"); // "my" or "company"

  // Live Business Logic: Exclude Weekends
  const workingDays = getWorkingDays(form.startDate, form.endDate);

  async function submit(e) {
    e.preventDefault();
    await request("/leave-request", { method: "POST", body: JSON.stringify({ ...form, personelId: currentPersonel.id }) });
    setIsOpen(false);
    done("Leave request submitted. Pending manager approval.");
  }

  // Filter leaves to show ONLY current personnel's past leaves
  const myLeaves = data.leaves.filter(l => l.personelId === currentPersonel.id || (l.firstName === currentPersonel.firstName && l.lastName === currentPersonel.lastName));

  return (
    <div className="panel" style={{ display: 'grid', gap: '24px' }}>
      {showTabs && (
        <div style={{ display: 'flex', gap: '16px', borderBottom: '1px solid var(--line)', paddingBottom: '16px' }}>
          <button className={activeTab === "my" ? "" : "secondary"} onClick={() => setActiveTab("my")}>My Leave Requests</button>
          <button className={activeTab === "company" ? "" : "secondary"} onClick={() => setActiveTab("company")}>Company Leaves</button>
        </div>
      )}

      {(!showTabs || activeTab === "my") && (
        <>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h2>My Leave Requests</h2>
            <button onClick={() => setIsOpen(true)}><Icons.Plus /> Request Leave</button>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Dates</th>
                  <th>Requested Days</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {myLeaves.map(l => (
                  <tr key={l.id}>
                    <td><StatusBadge value={l.leaveType} /></td>
                    <td>{l.startDate} / {l.endDate}</td>
                    <td>{l.requestedDays}</td>
                    <td><StatusBadge value={l.leaveStatus || "ONAY_BEKLIYOR"} /></td>
                  </tr>
                ))}
                {myLeaves.length === 0 && <tr><td colSpan="4" style={{ textAlign: 'center', color: 'var(--muted)' }}>No leave request history found.</td></tr>}
              </tbody>
            </table>
          </div>
        </>
      )}

      {showTabs && activeTab === "company" && (
        <>
          <h2>Company-wide Leave List</h2>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Employee</th>
                  <th>Type</th>
                  <th>Dates</th>
                  <th>Requested Days</th>
                  <th>Note / Description</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {data.leaves.map(l => (
                  <tr key={l.id}>
                    <td><strong>{l.firstName} {l.lastName}</strong></td>
                    <td><StatusBadge value={l.leaveType} /></td>
                    <td>{l.startDate} / {l.endDate}</td>
                    <td>{l.requestedDays}</td>
                    <td>{l.managerNote || '-'}</td>
                    <td><StatusBadge value={l.leaveStatus || "ONAY_BEKLIYOR"} /></td>
                  </tr>
                ))}
                {data.leaves.length === 0 && <tr><td colSpan="6" style={{ textAlign: 'center', color: 'var(--muted)' }}>No records found.</td></tr>}
              </tbody>
            </table>
          </div>
        </>
      )}

      <Modal title="Leave Request Form" isOpen={isOpen} onClose={() => setIsOpen(false)}>
        <form className="form-grid" onSubmit={submit}>
          <label className="wide">Leave Type
            <select value={form.leaveType} onChange={e => setForm({ ...form, leaveType: e.target.value })}>
              <option value="YILLIK_IZIN">Annual Leave</option>
              <option value="MAZERET_IZNI">Casual / Excuse Leave</option>
              <option value="HASTALIK_IZNI">Sick Leave</option>
            </select>
          </label>
          <label>Start Date
            <input type="date" min={today} value={form.startDate} onChange={e => setForm({ ...form, startDate: e.target.value })} required />
          </label>
          <label>End Date
            <input type="date" min={form.startDate || today} value={form.endDate} onChange={e => setForm({ ...form, endDate: e.target.value })} required />
          </label>

          <div className="wide message" style={{ background: '#f8fafc', borderColor: 'var(--line)', color: 'var(--ink)' }}>
            <strong>Calculated Working Days:</strong> {workingDays} Days (Weekends excluded)
          </div>

          <label className="wide">Note for Manager (Optional)
            <textarea value={form.managerNote} onChange={e => setForm({ ...form, managerNote: e.target.value })} placeholder="e.g. Annual summer vacation..." />
          </label>
          <div className="form-actions">
            <button type="button" className="secondary" onClick={() => setIsOpen(false)}>Cancel</button>
            <button type="submit" disabled={workingDays <= 0}>Submit</button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

// --- MODULE 4: ROOMS ---
function Rooms({ data, request, done, currentPersonel }) {
  const today = new Date().toISOString().split('T')[0];
  const [selectedDate, setSelectedDate] = useState(today);
  const [isOpen, setIsOpen] = useState(false);
  const [selectedMeeting, setSelectedMeeting] = useState(null);
  const [form, setForm] = useState({ roomId: "", name: "", date: today, start: "09:00", end: "10:00", purpose: "" });
  const [conflict, setConflict] = useState("");
  const [isEditMode, setIsEditMode] = useState(false);
  const [editMeetingId, setEditMeetingId] = useState(null);

  function handleSlotClick(room, h) {
    setIsEditMode(false);
    setEditMeetingId(null);
    setForm({ ...form, roomId: room.id, date: selectedDate, start: `${h.toString().padStart(2, '0')}:00`, end: `${(h + 1).toString().padStart(2, '0')}:00` });
    setConflict("");
    setIsOpen(true);
  }

  // Live Frontend Validation Check
  const liveConflict = useMemo(() => {
    if (!form.roomId || !form.date || !form.start || !form.end) return "";
    const sTime = new Date(`${form.date}T${form.start}:00`);
    const eTime = new Date(`${form.date}T${form.end}:00`);
    if (sTime >= eTime) return "End time must be after start time.";

    const overlapping = data.reservations.some(r => {
      if (isEditMode && r.id === editMeetingId) return false;
      if (r.roomId !== Number(form.roomId) && r.roomName !== data.rooms.find(ro => ro.id === Number(form.roomId))?.name) return false;
      const rs = new Date(r.startTime);
      const re = new Date(r.endTime);
      return (sTime < re && eTime > rs);
    });
    return overlapping ? "This room is occupied during the selected time range." : "";
  }, [form, data.reservations, data.rooms]);

  async function submit(e) {
    e.preventDefault();
    if (liveConflict) return;

    try {
      if (isEditMode) {
        await request(`/room-reservation/update/${editMeetingId}`, {
          method: "PUT",
          body: JSON.stringify({
            roomId: Number(form.roomId),
            personelId: currentPersonel.id,
            name: form.name,
            startTime: `${form.date}T${form.start}:00`,
            endTime: `${form.date}T${form.end}:00`,
            purpose: form.purpose
          })
        });
        setIsOpen(false);
        done("Reservation updated successfully.");
      } else {
        await request("/room-reservation", {
          method: "POST",
          body: JSON.stringify({
            roomId: Number(form.roomId),
            personelId: currentPersonel.id,
            name: form.name,
            startTime: `${form.date}T${form.start}:00`,
            endTime: `${form.date}T${form.end}:00`,
            purpose: form.purpose
          })
        });
        setIsOpen(false);
        done("Reservation created successfully.");
      }
    } catch (err) {
      setConflict(err.message || "This room is occupied during the selected time range.");
    }
  }

  const hours = Array.from({ length: 10 }, (_, i) => i + 9);

  // Make sure start date/time formatting is robust
  function formatTime(dateTimeStr) {
    if (!dateTimeStr) return "";
    try {
      const parts = dateTimeStr.split('T');
      if (parts.length > 1) {
        return parts[1].substring(0, 5);
      }
      return new Date(dateTimeStr).toTimeString().substring(0, 5);
    } catch (e) {
      return "";
    }
  }

  async function deleteReservation(id) {
    if (!window.confirm("Are you sure you want to cancel this reservation?")) return;
    try {
      await request(`/room-reservation/delete/${id}`, { method: "DELETE" });
      setSelectedMeeting(null);
      done("Reservation cancelled.");
    } catch(err) {
      alert("Error: " + err.message);
    }
  }

  function handleEditSelected() {
    setIsEditMode(true);
    setEditMeetingId(selectedMeeting.id);
    const sd = selectedMeeting.startTime.split('T')[0];
    const st = formatTime(selectedMeeting.startTime);
    const et = formatTime(selectedMeeting.endTime);
    setForm({
      roomId: selectedMeeting.roomId || data.rooms.find(r => r.name === selectedMeeting.roomName)?.id || "",
      name: selectedMeeting.name,
      date: sd,
      start: st,
      end: et,
      purpose: selectedMeeting.purpose || ""
    });
    setSelectedMeeting(null);
    setIsOpen(true);
    setConflict("");
  }

  function getColSpan(start, end) {
    const s = new Date(start); const e = new Date(end);
    const startHour = s.getHours(); const startMin = s.getMinutes() / 60;
    const endHour = e.getHours(); const endMin = e.getMinutes() / 60;
    const startTotal = startHour + startMin;
    const endTotal = endHour + endMin;
    const colStart = Math.max(1, Math.min(11, startTotal - 9 + 1));
    const colSpan = Math.max(0.5, endTotal - startTotal);
    return { gridColumnStart: colStart + 1, width: `${colSpan * 10}%` };
  }

  return (
    <div className="panel" style={{ display: 'grid', gap: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <h2>Meeting Room Schedule</h2>
        <div style={{ display: 'flex', gap: '16px', alignItems: 'center', flexWrap: 'wrap' }}>
          <label style={{ display: 'flex', gap: '8px', alignItems: 'center', margin: 0, fontSize: '14px', fontWeight: '600' }}>
            Select Date:
            <input type="date" value={selectedDate} onChange={e => setSelectedDate(e.target.value)} style={{ width: '160px', minHeight: '34px', padding: '4px 8px' }} />
          </label>
          <button onClick={() => { setIsEditMode(false); setEditMeetingId(null); setConflict(""); setForm({ ...form, date: selectedDate }); setIsOpen(true); }}><Icons.Plus /> New Reservation</button>
        </div>
      </div>

      <div className="timeline">
        <div className="timeline-header">
          <div className="timeline-cell" style={{ background: 'white', borderBottom: '2px solid var(--line)' }}>Room</div>
          {hours.map(h => <div key={h} className="timeline-cell" style={{ borderBottom: '2px solid var(--line)' }}>{h}:00</div>)}
        </div>
        {data.rooms.map(room => {
          const res = data.reservations.filter(r => r.roomName === room.name && r.startTime.startsWith(selectedDate));
          return (
            <div key={room.id} className="timeline-row">
              <div className="timeline-cell room-name">{room.name}</div>
              <div style={{ gridColumn: '2 / -1', display: 'grid', gridTemplateColumns: 'repeat(10, 1fr)', position: 'relative' }}>
                {hours.map(h => {
                  // Block out slots that have ANY reservations in this hour for visual cues
                  const isOccupied = res.some(r => {
                    const sh = new Date(r.startTime).getHours();
                    return sh === h;
                  });
                  return <div key={h} className={`timeline-slot ${isOccupied ? 'occupied' : ''}`} onClick={() => !isOccupied && handleSlotClick(room, h)}></div>
                })}
                {res.map(r => {
                  const style = getColSpan(r.startTime, r.endTime);
                  const colors = ['#165c7d', '#2f8f6f', '#b7791f', '#9333ea', '#e11d48', '#0284c7', '#ea580c'];
                  const color = colors[(r.id || 0) % colors.length];
                  return <div key={r.id} className="timeline-event" style={{ left: `calc((${style.gridColumnStart} - 2) * 10%)`, width: style.width, background: color, cursor: 'pointer' }} title="Click to view details" onClick={(e) => { e.stopPropagation(); setSelectedMeeting(r); }}>
                    {r.name}
                  </div>
                })}
              </div>
            </div>
          )
        })}
      </div>

      <Modal title={isEditMode ? "Update Reservation" : "Room Reservation"} isOpen={isOpen} onClose={() => setIsOpen(false)}>
        <div style={{ background: (liveConflict || conflict) ? '#fee2e2' : 'transparent', padding: (liveConflict || conflict) ? '12px' : '0', borderRadius: '8px', transition: 'background 0.3s' }}>
          {(liveConflict || conflict) && <div style={{ color: 'var(--danger)', fontWeight: 'bold', marginBottom: '16px' }}><Icons.X /> {liveConflict || conflict}</div>}
          <form className="form-grid" onSubmit={submit}>
            <label className="wide">Created By
              <input value={`${currentPersonel.firstName} ${currentPersonel.lastName}`} disabled style={{ background: '#f8fafc', color: 'var(--muted)' }} />
            </label>
            <label className="wide">Room
              <select value={form.roomId} onChange={e => setForm({ ...form, roomId: e.target.value })} required>
                <option value="">Select</option>
                {data.rooms.map(r => <option key={r.id} value={r.id}>{r.name} (Cap: {r.capacity})</option>)}
              </select>
            </label>
            <label className="wide">Meeting Title
              <input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} required />
            </label>
            <label className="wide">Date
              <input type="date" min={today} value={form.date} onChange={e => setForm({ ...form, date: e.target.value })} required />
            </label>
            <label>Start Time
              <input type="time" value={form.start} onChange={e => setForm({ ...form, start: e.target.value })} required />
            </label>
            <label>End Time
              <input type="time" value={form.end} onChange={e => setForm({ ...form, end: e.target.value })} required />
            </label>
            <label className="wide">Meeting Purpose / Notes
              <textarea value={form.purpose} onChange={e => setForm({ ...form, purpose: e.target.value })} />
            </label>
            <div className="form-actions">
              <button type="button" className="secondary" onClick={() => setIsOpen(false)}>Cancel</button>
              <button type="submit" disabled={!!liveConflict}>Save</button>
            </div>
          </form>
        </div>
      </Modal>

      <Modal title="Meeting Details" isOpen={!!selectedMeeting} onClose={() => setSelectedMeeting(null)}>
        {selectedMeeting && (
          <div style={{ display: 'grid', gap: '16px' }}>
            <div>
              <strong style={{ color: 'var(--muted)', fontSize: '12px', textTransform: 'uppercase' }}>Meeting Title</strong>
              <div style={{ fontSize: '18px', fontWeight: '700', color: 'var(--brand)', marginTop: '4px' }}>{selectedMeeting.name}</div>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
              <div>
                <strong style={{ color: 'var(--muted)', fontSize: '12px', textTransform: 'uppercase' }}>Room</strong>
                <div style={{ marginTop: '4px', fontWeight: '600' }}>{selectedMeeting.roomName}</div>
              </div>
              <div>
                <strong style={{ color: 'var(--muted)', fontSize: '12px', textTransform: 'uppercase' }}>Organizer</strong>
                <div style={{ marginTop: '4px', fontWeight: '600' }}>{selectedMeeting.firstName} {selectedMeeting.lastName}</div>
              </div>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
              <div>
                <strong style={{ color: 'var(--muted)', fontSize: '12px', textTransform: 'uppercase' }}>Date</strong>
                <div style={{ marginTop: '4px', fontWeight: '600' }}>{selectedMeeting.startTime ? selectedMeeting.startTime.split('T')[0] : ''}</div>
              </div>
              <div>
                <strong style={{ color: 'var(--muted)', fontSize: '12px', textTransform: 'uppercase' }}>Time Range</strong>
                <div style={{ marginTop: '4px', fontWeight: '600' }}>
                  {formatTime(selectedMeeting.startTime)} - {formatTime(selectedMeeting.endTime)}
                </div>
              </div>
            </div>
            <div>
              <strong style={{ color: 'var(--muted)', fontSize: '12px', textTransform: 'uppercase' }}>Meeting Purpose / Notes</strong>
              <div style={{ background: '#f8fafc', padding: '12px', borderRadius: '8px', border: '1px solid var(--line)', marginTop: '6px', whiteSpace: 'pre-wrap', fontSize: '14px', lineHeight: '1.5' }}>
                {selectedMeeting.purpose || 'No description provided.'}
              </div>
            </div>
            <div className="form-actions">
              {selectedMeeting.personelId === currentPersonel.id && (
                <>
                  <button className="secondary" style={{ borderColor: 'var(--danger)', color: 'var(--danger)' }} onClick={() => deleteReservation(selectedMeeting.id)}>Cancel Reservation</button>
                  <button className="secondary" onClick={handleEditSelected}>Update</button>
                </>
              )}
              <button className="secondary" onClick={() => setSelectedMeeting(null)}>Close</button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}

// --- MODULE 5: EQUIPMENT (Personel/Manager Roles) ---
function Equipment({ data, currentPersonel, isAdmin, isManager, mockEqRequests, setMockEqRequests, done, request }) {
  const [isOpen, setIsOpen] = useState(false);
  const [form, setForm] = useState({ equipmentId: "", purpose: "" });
  const showTabs = isAdmin || isManager;
  const [activeTab, setActiveTab] = useState("my"); // "my" or "company"
  const [dbRequests, setDbRequests] = useState([]);

  useEffect(() => {
    if (currentPersonel?.id) {
      fetchRequests();
    }
  }, [currentPersonel]);

  async function fetchRequests() {
    try {
      const res = await request(`/equipment-assignments/personel/${currentPersonel.id}`);
      setDbRequests(res || []);
    } catch (err) {
      console.error(err);
    }
  }

  async function submitRequest(e) {
    e.preventDefault();
    try {
      const requestData = {
        equipmentId: Number(form.equipmentId),
        personelId: currentPersonel.id,
        purpose: form.purpose
      };
      await request('/equipment-assignments/request', { method: 'POST', body: JSON.stringify(requestData) });
      setIsOpen(false);
      setForm({ equipmentId: "", purpose: "" });
      done("Equipment request submitted successfully.");
      fetchRequests();
    } catch (err) {
      alert("Error: " + err.message);
    }
  }

  async function returnEquipment(id) {
    if (window.confirm("Are you sure you want to return this equipment?")) {
      try {
        await request(`/equipment-assignments/return/${id}`, { method: 'PUT' });
        done("Equipment returned successfully.");
        fetchRequests();
      } catch (err) {
        alert("Error: " + err.message);
      }
    }
  }

  // All My Requests
  const myRequests = dbRequests;
  // My Assigned (Approved)
  const myAssigned = dbRequests.filter(r => r.status === "ONAYLANDI" || r.status === "PERSONELDE");

  return (
    <div className="panel" style={{ display: 'grid', gap: '24px' }}>
      {showTabs && (
        <div style={{ display: 'flex', gap: '16px', borderBottom: '1px solid var(--line)', paddingBottom: '16px' }}>
          <button className={activeTab === "my" ? "" : "secondary"} onClick={() => setActiveTab("my")}>My Assigned Equipment</button>
          <button className={activeTab === "company" ? "" : "secondary"} onClick={() => setActiveTab("company")}>Company Inventory</button>
        </div>
      )}

      {(!showTabs || activeTab === "my") && (
        <>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
            <h2>My Assigned Equipment (Active)</h2>
            <button onClick={() => setIsOpen(true)}><Icons.Plus /> Request Equipment</button>
          </div>
          <div className="table-wrap" style={{ marginBottom: '32px' }}>
            <table>
              <thead>
                <tr>
                  <th>Equipment Name</th>
                  <th>Justification / Reason</th>
                  <th>Delivery Date</th>
                  <th>Status</th>
                  <th style={{ textAlign: 'right' }}>Action</th>
                </tr>
              </thead>
              <tbody>
                {myAssigned.map(r => (
                  <tr key={r.id}>
                    <td><strong>{r.equipmentName || r.typeName}</strong></td>
                    <td>{r.purpose}</td>
                    <td>{r.date}</td>
                    <td><StatusBadge value={r.status} /></td>
                    <td style={{ textAlign: 'right' }}>
                      <button className="secondary" style={{ fontSize: '12px', padding: '4px 8px' }} onClick={() => returnEquipment(r.id)}>Return</button>
                    </td>
                  </tr>
                ))}
                {myAssigned.length === 0 && <tr><td colSpan="5" style={{ textAlign: 'center', color: 'var(--muted)' }}>No active equipment assigned to you.</td></tr>}
              </tbody>
            </table>
          </div>

          <h2 style={{ marginBottom: '16px' }}>Request History</h2>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Equipment Name</th>
                  <th>Justification / Reason</th>
                  <th>Request Date</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {myRequests.map(r => (
                  <tr key={r.id}>
                    <td><strong>{r.equipmentName || r.typeName}</strong></td>
                    <td>{r.purpose}</td>
                    <td>{r.date}</td>
                    <td><StatusBadge value={r.status} /></td>
                  </tr>
                ))}
                {myRequests.length === 0 && <tr><td colSpan="4" style={{ textAlign: 'center', color: 'var(--muted)' }}>No past requests found.</td></tr>}
              </tbody>
            </table>
          </div>
        </>
      )}

      {showTabs && activeTab === "company" && (
        <>
          <h2>Inventory List (Company-wide)</h2>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Type</th>
                  <th>Brand & Model</th>
                  <th>Serial No</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {data.equipments.map(e => (
                  <tr key={e.id}>
                    <td>{e.id}</td>
                    <td>{e.typeName}</td>
                    <td><strong>{e.brand}</strong> {e.model}</td>
                    <td><span style={{ fontFamily: 'monospace', color: 'var(--muted)' }}>{e.serialNumber}</span></td>
                    <td><StatusBadge value={e.status || "DEPODA"} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}

      <Modal title="New Equipment Request" isOpen={isOpen} onClose={() => setIsOpen(false)}>
        <form className="form-grid" onSubmit={submitRequest}>
          <label className="wide">Requested Equipment
            <select value={form.equipmentId} onChange={e => setForm({ ...form, equipmentId: e.target.value })} required>
              <option value="">Select</option>
              {data.equipments.filter(e => !e.status || e.status === "DEPODA").map(e => <option key={e.id} value={e.id}>{e.brand} {e.model} - {e.serialNumber}</option>)}
            </select>
          </label>
          <label className="wide">Request Justification
            <textarea value={form.purpose} onChange={e => setForm({ ...form, purpose: e.target.value })} required placeholder="Please explain why you need this equipment..." />
          </label>
          <div className="form-actions">
            <button type="button" className="secondary" onClick={() => setIsOpen(false)}>Cancel</button>
            <button type="submit">Submit Request</button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

// --- MODULE 5.5: MANAGER APPROVALS SCREEN ---
function Approvals({ data, request, done, mockEqRequests, setMockEqRequests, currentPersonel }) {
  const [filterTab, setFilterTab] = useState("ALL"); // ALL, LEAVES, EQUIPMENTS

  async function approveLeave(id, status) {
    await request(`/leave-request/update/status/${id}?newStatus=${status}`, { method: "PUT" });
    done("Leave request status updated.");
  }

  async function approveEq(id, status) {
    try {
      await request(`/equipment-assignments/update/status/${id}?newStatus=${status}`, { method: "PUT" });
      done("Equipment request status updated.");
    } catch (err) {
      alert("Error: " + err.message);
    }
  }

  // We show pending items first, but keep processed ones in the list so they don't disappear immediately
  const filteredLeaves = filterTab === "EQUIPMENTS" ? [] : data.leaves;
  const filteredEq = filterTab === "LEAVES" ? [] : mockEqRequests;

  return (
    <div className="panel" style={{ display: 'grid', gap: '24px' }}>
      <div style={{ display: 'flex', gap: '16px', borderBottom: '1px solid var(--line)', paddingBottom: '16px' }}>
        <button className={filterTab === "ALL" ? "" : "secondary"} onClick={() => setFilterTab("ALL")}>All</button>
        <button className={filterTab === "LEAVES" ? "" : "secondary"} onClick={() => setFilterTab("LEAVES")}>Leaves</button>
        <button className={filterTab === "EQUIPMENTS" ? "" : "secondary"} onClick={() => setFilterTab("EQUIPMENTS")}>Equipment</button>
      </div>

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Type</th>
              <th>Employee</th>
              <th>Details / Date</th>
              <th>Status</th>
              <th style={{ textAlign: 'right' }}>Action</th>
            </tr>
          </thead>
          <tbody>
            {filteredLeaves.map(l => (
              <tr key={`l-${l.id}`}>
                <td><span className="badge info">Leave</span></td>
                <td>{l.firstName} {l.lastName}</td>
                <td><StatusBadge value={l.leaveType} /> ({l.startDate} - {l.endDate})</td>
                <td><StatusBadge value={l.leaveStatus || "BEKLIYOR"} /></td>
                <td style={{ textAlign: 'right' }}>
                  {(l.leaveStatus === "BEKLIYOR" || l.leaveStatus === "ONAY_BEKLIYOR") ? (
                    l.personelId === currentPersonel.id ? (
                      <span style={{ fontSize: '12px', color: 'var(--danger)' }}>You cannot approve your own request</span>
                    ) : (
                      <div style={{ display: 'inline-flex', gap: '8px' }}>
                        <button className="icon-btn approve" onClick={() => approveLeave(l.id, "ONAYLANDI")}><Icons.Check /></button>
                        <button className="icon-btn danger" onClick={() => approveLeave(l.id, "REDDEDILDI")}><Icons.X /></button>
                      </div>
                    )
                  ) : (
                    <StatusBadge value={l.leaveStatus} />
                  )}
                </td>
              </tr>
            ))}

            {filteredEq.map(eq => (
              <tr key={`e-${eq.id}`}>
                <td><span className="badge orange">Equipment</span></td>
                <td>{eq.personelFullName}</td>
                <td>{eq.typeName} ({eq.purpose})</td>
                <td><StatusBadge value={eq.status} /></td>
                <td style={{ textAlign: 'right' }}>
                  {eq.status === "BEKLIYOR" ? (
                    eq.personelId === currentPersonel.id ? (
                      <span style={{ fontSize: '12px', color: 'var(--danger)' }}>You cannot approve your own request</span>
                    ) : (
                      <div style={{ display: 'inline-flex', gap: '8px' }}>
                        <button className="icon-btn approve" onClick={() => approveEq(eq.id, "ONAYLANDI")}><Icons.Check /></button>
                        <button className="icon-btn danger" onClick={() => approveEq(eq.id, "REDDEDILDI")}><Icons.X /></button>
                      </div>
                    )
                  ) : (
                    <StatusBadge value={eq.status} />
                  )}
                </td>
              </tr>
            ))}

            {(filteredLeaves.length === 0 && filteredEq.length === 0) && (
              <tr><td colSpan="5" style={{ textAlign: 'center', color: 'var(--muted)' }}>No records found.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

// --- MODULE 6: ANNOUNCEMENTS ---
function Announcements({ data, request, done, currentPersonel, isAdmin }) {
  const [isOpen, setIsOpen] = useState(false);
  const [form, setForm] = useState({ title: "", content: "" });
  const [localData, setLocalData] = useState(data.announcements);
  const [activeCommentId, setActiveCommentId] = useState(null);
  const [commentText, setCommentText] = useState("");

  useEffect(() => { setLocalData(data.announcements); }, [data.announcements]);

  async function submit(e) {
    e.preventDefault();
    await request("/announcement", { method: "POST", body: JSON.stringify({ ...form, personelId: currentPersonel.id }) });
    setIsOpen(false);
    setForm({ title: "", content: "" });
    done("Announcement published.");
  }

  async function handleLike(id) {
    setLocalData(prev => prev.map(a => {
      if (a.id !== id) return a;
      const wasLiked = a.userReaction === "like";
      return {
        ...a,
        likeCount: wasLiked ? (a.likeCount || 0) - 1 : (a.likeCount || 0) + 1,
        userReaction: wasLiked ? null : "like"
      };
    }));
    try { await request(`/announcement/like/${id}`, { method: "PATCH" }); } catch (e) { }
  }

  async function postComment(e, annId) {
    e.preventDefault();
    if (!commentText.trim()) return;
    await request("/comment", { method: "POST", body: JSON.stringify({ content: commentText, announcementId: annId, personelId: currentPersonel.id }) });
    setCommentText("");
    done("Comment added.");
  }

  function getInitials(name) {
    if (!name) return "U";
    return name.split(" ").map(n => n[0]).join("").toUpperCase().substring(0, 2);
  }

  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1fr) 300px', gap: '24px', alignItems: 'start' }}>
      <div className="feed-list">
        {localData.map(a => (
          <div key={a.id} className="feed-card shadow">
            <div className="feed-header">
              <div className="feed-avatar">{getInitials(a.authorName)}</div>
              <div className="feed-meta">
                <b>{a.authorName || "System"}</b>
                <span>{new Date(a.creationTime).toLocaleString()}</span>
              </div>
            </div>
            <div className="feed-title">{a.title}</div>
            <div className="feed-content">{a.content}</div>

            <div className="feed-actions">
              <button className="feed-btn" style={a.userReaction === 'like' ? { color: 'var(--brand)' } : {}} onClick={() => handleLike(a.id)}>
                <Icons.Heart solid={a.userReaction === 'like'} />
                {a.likeCount || 0} Like
              </button>
              <button className="feed-btn" onClick={() => setActiveCommentId(activeCommentId === a.id ? null : a.id)}>
                <Icons.Chat /> {(a.comments || []).length} Comments
              </button>
            </div>

            {activeCommentId === a.id && (
              <div className="comments">
                {(a.comments || []).map(c => (
                  <div key={c.id} className="comment">
                    <div className="comment-avatar">{getInitials(c.authorName)}</div>
                    <div className="comment-body">
                      <b>{c.authorName}</b>
                      <p>{c.content}</p>
                    </div>
                  </div>
                ))}
                <form className="comment-input" onSubmit={(e) => postComment(e, a.id)}>
                  <input placeholder="Write a comment..." value={commentText} onChange={e => setCommentText(e.target.value)} required />
                  <button type="submit" className="secondary" style={{ borderRadius: '20px' }}>Post</button>
                </form>
              </div>
            )}
          </div>
        ))}
        {localData.length === 0 && <div className="panel">No announcements yet.</div>}
      </div>

      {isAdmin && (
        <div className="panel">
          <h3>Have an announcement?</h3>
          <p style={{ color: 'var(--muted)', fontSize: '13px', marginBottom: '16px' }}>Share important updates with the entire company.</p>
          <button style={{ width: '100%' }} onClick={() => setIsOpen(true)}><Icons.Plus /> Publish Announcement</button>
        </div>
      )}

      <Modal title="New Announcement" isOpen={isOpen} onClose={() => setIsOpen(false)}>
        <form className="form-grid" onSubmit={submit}>
          <label className="wide">Title
            <input value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} required placeholder="e.g. Q3 Company Meeting" />
          </label>
          <label className="wide">Content
            <textarea value={form.content} onChange={e => setForm({ ...form, content: e.target.value })} required placeholder="Details..." />
          </label>
          <div className="form-actions">
            <button type="button" className="secondary" onClick={() => setIsOpen(false)}>Cancel</button>
            <button type="submit">Publish</button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

// --- MODULE 7: PROFILE ---
function Profile({ data, currentPersonel, request, done }) {
  const [activeTab, setActiveTab] = useState("personal");

  // Security Tab State
  const [password, setPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPw1, setShowPw1] = useState(false);
  const [showPw2, setShowPw2] = useState(false);
  const [showPw3, setShowPw3] = useState(false);
  const [msg, setMsg] = useState("");
  const [err, setErr] = useState("");

  // Personal Tab State
  const [allSkills, setAllSkills] = useState([]);

  useEffect(() => {
    request("/skill/get/all")
      .then(data => setAllSkills(Array.isArray(data) ? data : data.data || []))
      .catch(err => console.error(err));
  }, [request]);

  const [firstName, setFirstName] = useState(currentPersonel.firstName || "");
  const [lastName, setLastName] = useState(currentPersonel.lastName || "");
  const [email, setEmail] = useState(currentPersonel.email || "");
  const [birthOfDate, setBirthOfDate] = useState(currentPersonel.birthOfDate || "");
  const [previewImage, setPreviewImage] = useState(currentPersonel.profilePhoto || "");
  const [userSkills, setUserSkills] = useState(currentPersonel.skills || []);
  const [newSkill, setNewSkill] = useState("");
  const [personalMsg, setPersonalMsg] = useState("");
  const [personalErr, setPersonalErr] = useState("");

  function getInitials(p) {
    if (!p.firstName) return "U";
    return `${p.firstName[0]}${(p.lastName || " ")[0]}`.toUpperCase();
  }

  function handlePhotoChange(e) {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  }

  async function submitPassword(e) {
    e.preventDefault();
    setMsg(""); setErr("");
    if (!password || !newPassword || !confirmPassword) return setErr("Please fill in all fields.");
    if (newPassword !== confirmPassword) return setErr("New passwords do not match.");
    try {
      await request(`/personel/change-password/${currentPersonel.id}`, {
        method: "PUT",
        body: JSON.stringify({
          oldPassword: password,
          newPassword: newPassword
        })
      });
      setMsg("Password updated successfully.");
      setPassword(""); setNewPassword(""); setConfirmPassword("");
      done("Password updated.");
    } catch (error) {
      setErr(error.message || "An error occurred while updating password.");
    }
  }

  async function submitPersonal(e) {
    e.preventDefault();
    setPersonalMsg(""); setPersonalErr("");
    try {
      const dept = data?.departments?.find(d => d.name === currentPersonel.departmentName);
      const role = data?.roles?.find(r => r.name === currentPersonel.roleName);

      const skillIds = userSkills.map(skName => {
        const found = allSkills.find(sk => (sk.name || sk) === skName);
        return found ? found.id : null;
      }).filter(id => id != null);

      await request(`/personel/update/${currentPersonel.id}`, {
        method: "PUT",
        body: JSON.stringify({
          firstName,
          lastName,
          email,
          birthOfDate,
          profilePhoto: previewImage,
          departmentId: dept ? dept.id : null,
          roleId: role ? role.id : null,
          skillIds: skillIds
        })
      });
      setPersonalMsg("Personal information updated successfully.");
      done("Personal information updated.");
    } catch (error) {
      setPersonalErr(error.message || "An error occurred during update.");
    }
  }

  function addSkill() {
    if (!newSkill.trim()) return;
    if (!userSkills.includes(newSkill.trim())) {
      setUserSkills([...userSkills, newSkill.trim()]);
    }
    setNewSkill("");
  }

  function removeSkill(skillToRemove) {
    setUserSkills(userSkills.filter(s => s !== skillToRemove));
  }

  const availableSkills = allSkills.filter(sk => {
    const skillName = typeof sk === 'string' ? sk : sk.name;
    return !userSkills.some(s => {
      const sName = typeof s === 'string' ? s : s.name;
      return sName === skillName;
    });
  });

  return (
    <div style={{ display: 'grid', gap: '24px', maxWidth: '1000px', margin: '0 auto', width: '100%' }}>
      {/* TABS */}
      <div style={{ display: 'flex', gap: '16px', borderBottom: '1px solid var(--line)' }}>
        <button
          onClick={() => setActiveTab("personal")}
          style={{
            background: 'none', border: 'none', padding: '12px 24px', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer',
            borderBottom: activeTab === "personal" ? '3px solid var(--brand)' : '3px solid transparent',
            color: activeTab === "personal" ? 'var(--brand)' : 'var(--muted)'
          }}
        >
          Personal Info
        </button>
        <button
          onClick={() => setActiveTab("security")}
          style={{
            background: 'none', border: 'none', padding: '12px 24px', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer',
            borderBottom: activeTab === "security" ? '3px solid var(--brand)' : '3px solid transparent',
            color: activeTab === "security" ? 'var(--brand)' : 'var(--muted)'
          }}
        >
          Security & Password
        </button>
      </div>

      {activeTab === "personal" && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {/* Left Side: Avatar & Summary */}
          <div className="panel" style={{ display: 'flex', flexDirection: 'column', gap: '16px', alignItems: 'center', textAlign: 'center' }}>
            <div style={{ position: 'relative', width: '120px', height: '120px' }}>
              {previewImage ? (
                <img src={previewImage} alt="Avatar" style={{ width: '100%', height: '100%', borderRadius: '50%', objectFit: 'cover', border: '3px solid white', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }} />
              ) : (
                <div style={{ width: '100%', height: '100%', borderRadius: '50%', background: 'var(--brand)', color: 'white', display: 'grid', placeItems: 'center', fontSize: '40px', fontWeight: 'bold', border: '3px solid white', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }}>
                  {getInitials(currentPersonel)}
                </div>
              )}
              {/* Camera Icon Overlay with hidden input */}
              <label
                title="Change Photo"
                style={{ position: 'absolute', bottom: '0', right: '0', background: 'var(--brand)', color: 'white', border: 'none', borderRadius: '50%', width: '36px', height: '36px', display: 'grid', placeItems: 'center', cursor: 'pointer', boxShadow: '0 2px 4px rgba(0,0,0,0.2)' }}
              >
                <Icons.Camera size={18} />
                <input type="file" style={{ display: 'none' }} accept="image/png, image/jpeg, image/jpg" onChange={handlePhotoChange} />
              </label>
            </div>

            <div>
              <h2 style={{ margin: '0 0 4px', fontSize: '22px' }}>{currentPersonel.firstName} {currentPersonel.lastName}</h2>
              <div style={{ display: 'inline-block', background: 'var(--brand)', color: 'white', padding: '4px 12px', borderRadius: '999px', fontSize: '13px', fontWeight: 'bold', marginBottom: '8px' }}>
                {currentPersonel.roleName || "No Role Assigned"}
              </div>
              <div style={{ color: 'var(--muted)', fontSize: '14px' }}>
                Registration No: {currentPersonel.registrationNumber || currentPersonel.sicilNo || "-"}
              </div>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '24px' }}>
            <div className="panel" style={{ flex: 1 }}>
              <h3 style={{ borderBottom: '1px solid var(--line)', paddingBottom: '12px', marginBottom: '20px' }}>Update Personal Information</h3>
              {personalMsg && <div className="message" style={{ marginBottom: '16px' }}><Icons.Check /> {personalMsg}</div>}
              {personalErr && <div className="message error" style={{ marginBottom: '16px' }}><Icons.X size={16} /> {personalErr}</div>}

              <form onSubmit={submitPersonal} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {/* 1 Column Grid for Form Fields to fit side-by-side layout */}
                <div style={{ display: 'grid', gridTemplateColumns: '1fr', gap: '16px' }}>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    First Name
                    <input type="text" value={firstName} onChange={e => setFirstName(e.target.value)} required style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--line)' }} />
                  </label>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    Last Name
                    <input type="text" value={lastName} onChange={e => setLastName(e.target.value)} required style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--line)' }} />
                  </label>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    Email
                    <input type="email" value={email} onChange={e => setEmail(e.target.value)} required style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--line)' }} />
                  </label>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    Date of Birth
                    <input type="date" value={birthOfDate} onChange={e => setBirthOfDate(e.target.value)} style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--line)' }} />
                  </label>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    Registration No
                    <input type="text" value={currentPersonel.registrationNumber || currentPersonel.sicilNo || "-"} disabled style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--line)', background: 'var(--bg)', color: 'var(--muted)', cursor: 'not-allowed' }} />
                  </label>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    Department
                    <input type="text" value={currentPersonel.departmentName || "-"} disabled style={{ padding: '10px', borderRadius: '6px', border: '1px solid var(--line)', background: 'var(--bg)', color: 'var(--muted)', cursor: 'not-allowed' }} />
                  </label>
                </div>
                <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '8px' }}>
                  <button type="submit">Save Changes</button>
                </div>
              </form>
            </div>

            <div className="panel" style={{ flex: 1 }}>
              <h3 style={{ borderBottom: '1px solid var(--line)', paddingBottom: '12px', marginBottom: '20px' }}>Skills</h3>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', marginBottom: '16px' }}>
                {userSkills.map(s => (
                  <span key={s} style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'var(--brand)', color: 'white', padding: '6px 12px', borderRadius: '999px', fontSize: '14px', boxShadow: '0 1px 2px rgba(0,0,0,0.1)' }}>
                    {s}
                    <button
                      type="button"
                      onClick={() => removeSkill(s)}
                      style={{ background: 'transparent', border: 'none', color: 'white', display: 'flex', alignItems: 'center', padding: '0', cursor: 'pointer', opacity: '0.8', transition: 'opacity 0.2s' }}
                      onMouseEnter={e => e.currentTarget.style.opacity = '1'}
                      onMouseLeave={e => e.currentTarget.style.opacity = '0.8'}
                    >
                      <Icons.X size={14} />
                    </button>
                  </span>
                ))}
                {userSkills.length === 0 && <span style={{ fontSize: '14px', color: 'var(--muted)' }}>No skills added yet.</span>}
              </div>
              <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                <select
                  value={newSkill}
                  onChange={e => setNewSkill(e.target.value)}
                  style={{ flex: 1, padding: '10px', borderRadius: '6px', border: '1px solid var(--line)' }}
                >
                  <option value="">Select a Skill...</option>
                  {availableSkills.map(sk => {
                    const skName = typeof sk === 'string' ? sk : sk.name;
                    return (
                      <option key={sk.id || skName} value={skName}>
                        {skName}
                      </option>
                    );
                  })}
                </select>
                <button type="button" onClick={addSkill} className="secondary" style={{ padding: '10px 20px', borderRadius: '6px' }}>+ Add</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {activeTab === "security" && (
        <div className="panel">
          <h3 style={{ borderBottom: '1px solid var(--line)', paddingBottom: '12px', marginBottom: '20px' }}>Security & Password Management</h3>
          {msg && <div className="message" style={{ marginBottom: '16px' }}><Icons.Check /> {msg}</div>}
          {err && <div className="message error" style={{ marginBottom: '16px' }}><Icons.X size={16} /> {err}</div>}

          <form className="form-grid" onSubmit={submitPassword} style={{ maxWidth: '600px' }}>
            <label className="wide">Current Password
              <div style={{ display: 'flex', position: 'relative' }}>
                <input type={showPw1 ? "text" : "password"} value={password} onChange={e => setPassword(e.target.value)} required />
                <button type="button" onClick={() => setShowPw1(!showPw1)} style={{ position: 'absolute', right: '4px', top: '4px', padding: '6px', minHeight: 'auto', background: 'transparent', border: 'none', color: 'var(--muted)' }}>
                  {showPw1 ? <Icons.EyeOff /> : <Icons.Eye />}
                </button>
              </div>
            </label>

            <label className="wide">New Password
              <div style={{ display: 'flex', position: 'relative' }}>
                <input type={showPw2 ? "text" : "password"} value={newPassword} onChange={e => setNewPassword(e.target.value)} required />
                <button type="button" onClick={() => setShowPw2(!showPw2)} style={{ position: 'absolute', right: '4px', top: '4px', padding: '6px', minHeight: 'auto', background: 'transparent', border: 'none', color: 'var(--muted)' }}>
                  {showPw2 ? <Icons.EyeOff /> : <Icons.Eye />}
                </button>
              </div>
            </label>

            <label className="wide">Confirm New Password
              <div style={{ display: 'flex', position: 'relative' }}>
                <input type={showPw3 ? "text" : "password"} value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} required />
                <button type="button" onClick={() => setShowPw3(!showPw3)} style={{ position: 'absolute', right: '4px', top: '4px', padding: '6px', minHeight: 'auto', background: 'transparent', border: 'none', color: 'var(--muted)' }}>
                  {showPw3 ? <Icons.EyeOff /> : <Icons.Eye />}
                </button>
              </div>
            </label>
            <div className="form-actions"><button type="submit">Update Password</button></div>
          </form>
        </div>
      )}
    </div>
  );
}

// --- LOGIN ---
function Login({ onLogin, error, setError }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  async function submit(e) {
    e.preventDefault();
    try { await onLogin(email, password); }
    catch (err) { setError(err.message); }
  }
  return (
    <div className="login">
      <form className="login-box" onSubmit={submit}>
        <h1>Enterprise Portal</h1>
        <p style={{ textAlign: 'center', color: 'var(--muted)', fontSize: '14px', margin: 0 }}>Enter your credentials to sign in</p>
        {error && <div className="message error"><Icons.X /> {error}</div>}
        <label>Email<input value={email} onChange={e => setEmail(e.target.value)} required /></label>
        <label>Password<input type="password" value={password} onChange={e => setPassword(e.target.value)} required /></label>
        <button type="submit" style={{ marginTop: '8px' }}>Sign In</button>
      </form>
    </div>
  );
}

export default App;